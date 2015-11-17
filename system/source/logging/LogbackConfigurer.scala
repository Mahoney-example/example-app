package uk.org.lidalia
package exampleapp.system
package logging

import ch.qos.logback.classic.jul.LevelChangePropagator
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.classic.{Level, LoggerContext}
import ch.qos.logback.core.spi.{ContextAware, LifeCycle}
import ch.qos.logback.core.{AsyncAppenderBase, ConsoleAppender, UnsynchronizedAppenderBase}
import org.slf4j.{Logger, LoggerFactory}
import uk.org.lidalia.exampleapp.system.logging.JulConfigurer.sendJulToSlf4j

object LogbackConfigurer {

  private val logFactory = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]

  def configureLogback(applicationPackage: String) = {

    sendJulToSlf4j()

    logFactory.addListener(started(new LevelChangePropagator))

    val root = logFactory.getLogger(Logger.ROOT_LOGGER_NAME)
    root.setLevel(Level.WARN)
    val asyncConsoleAppender = started(new AsyncAppenderBase[ILoggingEvent] {
      override def preprocess(eventObject: ILoggingEvent): Unit = eventObject.prepareForDeferredProcessing()
    })

    val sysOutConsoleAppender = started(new ConsoleAppender[ILoggingEvent])
    sysOutConsoleAppender.setTarget("System.out")
    val sysErrConsoleAppender = started(new ConsoleAppender[ILoggingEvent])
    sysErrConsoleAppender.setTarget("System.err")

    asyncConsoleAppender.addAppender(started(new UnsynchronizedAppenderBase[ILoggingEvent] {
      override def append(eventObject: ILoggingEvent): Unit = {
        if (eventObject.getLevel.isGreaterOrEqual(Level.WARN)) {
          sysErrConsoleAppender.doAppend(eventObject)
        } else {
          sysOutConsoleAppender.doAppend(eventObject)
        }
      }
    }))

    root.addAppender(started(asyncConsoleAppender))

    val applicationLogger = logFactory.getLogger(applicationPackage)
    applicationLogger.setLevel(Level.INFO)

  }

  private def started[T <: LifeCycle with ContextAware](thing: T): T = {
    thing.setContext(logFactory)
    thing.start()
    thing
  }
}
