package uk.org.lidalia
package exampleapp


import net.{Uri, Port, Url}
import server.application.ApplicationConfig
import server.web.ServerDefinition
import server.web.ServerConfig
import system.db.JdbcConfig
import system.logging.LogbackConfigurer

import collection.immutable
import collection.JavaConversions.mapAsScalaMap
import collection.JavaConversions.propertiesAsScalaMap

object Main {

  LogbackConfigurer.configureLogback("uk.org.lidalia.exampleapp")

  def main(args: Array[String]) {

    val config = configFor(
      args.toVector,
      System.getProperties.toMap,
      System.getenv().toMap
    )

    val server = ServerDefinition(config)

    server.runUntilShutdown()
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
          Uri(s"jdbc:hsqldb:mem:local"),
          "sa",
          "",
          "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS"
        )
      ),

      localPort = Port(80)
    )
  }
}
