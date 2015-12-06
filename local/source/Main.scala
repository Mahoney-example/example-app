package uk.org.lidalia
package exampleapp
package local

import ch.qos.logback.classic.Level
import db.changelog.FastLiquibase
import org.slf4j.Logger.ROOT_LOGGER_NAME
import uk.org.lidalia.exampleapp.system.db.MemDatabaseDefinition
import uk.org.lidalia.exampleapp.system.logging.LogbackLoggingDefinition
import uk.org.lidalia.net.Port
import uk.org.lidalia.stubhttp.StubHttpServerFactory

object Main {

  def main(args: Array[String]) {

    val logbackLoggingDefinition = LogbackLoggingDefinition(
      ROOT_LOGGER_NAME -> Level.WARN,
      "uk.org.lidalia.exampleapp" -> Level.INFO,
      "WireMock" -> Level.INFO
    )

    logbackLoggingDefinition.using { loggerFactory =>

      FastLiquibase()

      val environment = EnvironmentDefinition(
        List(Port(8080)),
        loggerFactory,
        StubHttpServerFactory(Port(8082)),
        MemDatabaseDefinition("local")
      )

      environment.runUntilShutdown()
    }
  }
}
