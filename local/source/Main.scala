package uk.org.lidalia
package exampleapp
package local

import java.time.Instant

import ch.qos.logback.classic.Level
import org.slf4j.Logger.ROOT_LOGGER_NAME
import uk.org.lidalia.exampleapp.server.adapters.outbound.profiles.userProfileTableCreation
import system.db.changelog.FastLiquibase
import system.db.changelog.Migrator.changeLog
import system.db.hsqldb.HsqlDatabaseDefinition
import system.logging.LogbackLoggingDefinition
import uk.org.lidalia.net.Port
import uk.org.lidalia.stubhttp.StubHttpServerFactory

object Main {

  def main(args: Array[String]) {

    println(Instant.now())

    val logbackLoggingDefinition = LogbackLoggingDefinition(
      ROOT_LOGGER_NAME -> Level.WARN,
      "uk.org.lidalia.exampleapp" -> Level.INFO,
      "WireMock" -> Level.INFO
    )

    logbackLoggingDefinition.using { loggerFactory =>

      println(Instant.now())

      FastLiquibase()

      println(Instant.now())

      val environment = EnvironmentDefinition(
        List(Port(8080), Port(8081)),
        loggerFactory,
        StubHttpServerFactory(Port(8082)),
        HsqlDatabaseDefinition(changeLog(userProfileTableCreation), "local")
      )

      println(Instant.now())

      environment.runUntilShutdown()
    }
  }
}
