package uk.org.lidalia
package exampleapp


import ch.qos.logback.classic.Level
import net.{Uri, Port, Url}
import server.application.ApplicationConfig
import server.web.ServerDefinition
import server.web.ServerConfig
import system.db.JdbcConfig
import system.logging.LogbackLoggingDefinition

import collection.immutable
import collection.JavaConversions.mapAsScalaMap
import collection.JavaConversions.propertiesAsScalaMap

object Main {

  def main(args: Array[String]) {

    LogbackLoggingDefinition(
      "uk.org.lidalia.exampleapp" -> Level.INFO
    ).using { loggerFactory =>

      val config = configFor(
        args.toVector,
        System.getProperties.toMap,
        System.getenv().toMap
      )

      val server = ServerDefinition(config, loggerFactory)

      server.runUntilShutdown()
    }
  }

  private def configFor(
    args: immutable.Seq[String],
    sysProps: Map[String, String],
    env: Map[String, String]
  ): ServerConfig = {

    ServerConfig (
      ApplicationConfig(
        sendGridUrl = Url("http://www.example.com"),
        sendGridToken = "",
        jdbcConfig = JdbcConfig(
          Uri("jdbc:postgresql:local"),
          "sa",
          "",
          "SELECT 1"
        )
      ),

      localPort = Port(8080)
    )
  }
}
