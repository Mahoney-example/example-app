package uk.org.lidalia
package exampleapp.system
package logging

import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.jul.LevelChangePropagator
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.classic.{Level, LoggerContext}
import ch.qos.logback.core.{UnsynchronizedAppenderBase, AsyncAppenderBase, ConsoleAppender}
import ch.qos.logback.core.spi.{ContextAware, LifeCycle}
import org.slf4j.Logger
import scalalang.ResourceFactory
import uk.org.lidalia.exampleapp.system.logging.StaticLoggerFactory.factory
import uk.org.lidalia.scalalang.ResourceFactory._try

object LogbackLoggingDefinition {

  def apply(
    loggerLevels: (String, Level)*
  ): LogbackLoggingDefinition = {
    apply(
      factory.asInstanceOf[LoggerContext],
      loggerLevels:_*
    )
  }

  def apply(
    pattern: String,
    loggerLevels: (String, Level)*
  ): LogbackLoggingDefinition = {
    apply(
      factory.asInstanceOf[LoggerContext],
      pattern,
      loggerLevels:_*
    )
  }

  def apply(
    logFactory: LoggerContext,
    loggerLevels: (String, Level)*
  ): LogbackLoggingDefinition = {
    new LogbackLoggingDefinition(
      logFactory,
      "%d{ISO8601, UTC} [%-38thread] %-5level %-36logger{36} %msg%n",
      loggerLevels.toList
    )
  }

  def apply(
    logFactory: LoggerContext,
    pattern: String,
    loggerLevels: (String, Level)*
  ): LogbackLoggingDefinition = {
    new LogbackLoggingDefinition(
      logFactory,
      pattern,
      loggerLevels.toList
    )
  }
}

class LogbackLoggingDefinition private (
  logFactory: LoggerContext,
  pattern: String,
  loggerLevels: List[(String, Level)]
) extends ResourceFactory[LogbackLoggerFactory] {

  override def using[T](work: (LogbackLoggerFactory) => T): T = {

    logFactory.reset()

    val root = logFactory.getLogger(Logger.ROOT_LOGGER_NAME)
    root.setLevel(Level.WARN)
    root.detachAndStopAllAppenders()

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

    logFactory.addListener(started(new LevelChangePropagator))

    loggerLevels.foreach {
      case (loggerName, level) => logFactory.getLogger(loggerName).setLevel(level)
    }
    logFactory.start()

    _try {
      work(new LogbackLoggerFactory(logFactory))
    } _finally  {
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
    pl.setPattern(pattern)
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
