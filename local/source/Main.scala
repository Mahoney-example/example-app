package uk.org.lidalia
package exampleapp
package local

import ch.qos.logback.classic.{Level, LoggerContext}
import db.changelog.FastLiquibase
import net.Port
import org.slf4j.Logger.ROOT_LOGGER_NAME
import org.slf4j.LoggerFactory
import stubhttp.StubHttpServerFactory
import system.db.MemDatabaseDefinition
import system.logging.LogbackLoggingDefinition

object Main {

  FastLiquibase()

  def main(args: Array[String]) {

    val staticLoggerFactory = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]

    val logbackLoggingDefinition = LogbackLoggingDefinition(
      staticLoggerFactory,
      ROOT_LOGGER_NAME -> Level.WARN,
      "uk.org.lidalia.exampleapp" -> Level.INFO,
      "WireMock" -> Level.INFO
    )

    logbackLoggingDefinition.using { loggerFactory =>
      val environment = EnvironmentDefinition(
        Port(8080),
        loggerFactory,
        Port(8081),
        loggerFactory,
        StubHttpServerFactory(Port(8082)),
        MemDatabaseDefinition("local")
      )

      environment.runUntilShutdown()
    }
  }
}
