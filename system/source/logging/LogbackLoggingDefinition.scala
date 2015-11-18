package uk.org.lidalia
package exampleapp.system
package logging

import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.jul.LevelChangePropagator
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.classic.{Level, LoggerContext}
import ch.qos.logback.core.{UnsynchronizedAppenderBase, AsyncAppenderBase, ConsoleAppender}
import ch.qos.logback.core.spi.{ContextAware, LifeCycle}
import ch.qos.logback.core.status.OnErrorConsoleStatusListener
import org.slf4j.Logger
import scalalang.ResourceFactory

object LogbackLoggingDefinition {

  def apply(
    loggerLevels: (String, Level)*
  ): LogbackLoggingDefinition = {
    apply(new LoggerContext, loggerLevels:_*)
  }

  def apply(
    logFactory: LoggerContext,
    loggerLevels: (String, Level)*
  ): LogbackLoggingDefinition = {
    new LogbackLoggingDefinition(logFactory, loggerLevels.toList)
  }
}

class LogbackLoggingDefinition private (
  logFactory: LoggerContext,
  loggerLevels: List[(String, Level)]
) extends ResourceFactory[LogbackLoggerFactory] {

  override def using[T](work: (LogbackLoggerFactory) => T): T = {

    logFactory.getStatusManager.add(started(new OnErrorConsoleStatusListener))
    logFactory.start()
    logFactory.addListener(started(new LevelChangePropagator))

    val root = logFactory.getLogger(Logger.ROOT_LOGGER_NAME)
    root.setLevel(Level.WARN)

    val asyncConsoleAppender = new AsyncAppenderBase[ILoggingEvent] {
      override def preprocess(eventObject: ILoggingEvent): Unit = eventObject.prepareForDeferredProcessing()
    }
    asyncConsoleAppender.setName("root_async")
    asyncConsoleAppender.setContext(logFactory)

    val sysOutConsoleAppender = consoleAppender("System.out")
    val sysErrConsoleAppender = consoleAppender("System.err")

    val sysConsoleAppender = new UnsynchronizedAppenderBase[ILoggingEvent] {
      override def append(eventObject: ILoggingEvent): Unit = {
        if (eventObject.getLevel.isGreaterOrEqual(Level.WARN)) {
          sysErrConsoleAppender.doAppend(eventObject)
        } else {
          sysOutConsoleAppender.doAppend(eventObject)
        }
      }
    }
    sysConsoleAppender.setName("console")
    asyncConsoleAppender.addAppender(started(sysConsoleAppender))

    root.detachAndStopAllAppenders()
    root.addAppender(started(asyncConsoleAppender))

    loggerLevels.foreach {
      case (loggerName, level) => logFactory.getLogger(loggerName).setLevel(level)
    }

    try {
      work(new LogbackLoggerFactory(logFactory))
    } finally {
      logFactory.stop()
    }
  }

  private def consoleAppender(target: String) = {
    val ca: ConsoleAppender[ILoggingEvent] = new ConsoleAppender[ILoggingEvent]
    ca.setContext(logFactory)
    ca.setName(target)
    ca.setTarget(target)
    val pl: PatternLayoutEncoder = new PatternLayoutEncoder
    pl.setContext(logFactory)
    pl.setPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n")
    pl.start()
    ca.setEncoder(pl)
    ca.start()
    ca
  }

  private def started[T <: LifeCycle with ContextAware](thing: T): T = {
    thing.setContext(logFactory)
    thing.start()
    thing
  }
}

class LogbackLoggerFactory(
  context: LoggerContext
) extends LoggerFactory[ch.qos.logback.classic.Logger] {

  override def getLogger(name: String) = context.getLogger(name)

}
