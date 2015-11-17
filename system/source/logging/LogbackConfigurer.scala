package uk.org.lidalia
package exampleapp.system
package logging

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.classic.{Level, LoggerContext}
import ch.qos.logback.classic.jul.LevelChangePropagator
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.filter.Filter
import ch.qos.logback.core.spi.FilterReply.{ACCEPT, DENY}
import ch.qos.logback.core.spi.{FilterReply, ContextAware, LifeCycle}
import org.slf4j.{Logger, LoggerFactory}

object LogbackConfigurer {

  private val logFactory = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]

  def configureLogback(applicationPackage: String) = {

    JuliConfigurer.sendJuliToSlf4j()

    logFactory.addListener(started(new LevelChangePropagator))

    val root = logFactory.getLogger(Logger.ROOT_LOGGER_NAME)
    root.setLevel(Level.WARN)
    root.addAppender(consoleAppender("System.out", DENY, ACCEPT))
    root.addAppender(consoleAppender("System.err", ACCEPT, DENY))

    val applicationLogger = logFactory.getLogger(applicationPackage)
    applicationLogger.setLevel(Level.INFO)

  }

  private def consoleAppender(target: String, logErrors: FilterReply, logInfo: FilterReply): ConsoleAppender[ILoggingEvent] = {
    val sysOutConsoleAppender = started(new ConsoleAppender[ILoggingEvent])
    sysOutConsoleAppender.setTarget(target)
    sysOutConsoleAppender.addFilter(started(filter { e =>
      if (e.getLevel.isGreaterOrEqual(Level.WARN)) logErrors
      else logInfo
    }))
    sysOutConsoleAppender
  }

  private def started[T <: LifeCycle with ContextAware](thing: T): T = {
    thing.setContext(logFactory)
    thing.start()
    thing
  }

  private def filter(func: (ILoggingEvent) => FilterReply) = {
    new Filter[ILoggingEvent] {
      override def decide(e: ILoggingEvent): FilterReply = func(e)
    }
  }
}
