package uk.org.lidalia
package exampleapp

import net.{Uri, Port, Url}
import server.application.ApplicationConfig
import server.web.ServerDefinition
import server.web.ServerConfig
import system.JdbcConfig

import collection.immutable
import collection.JavaConversions.mapAsScalaMap
import collection.JavaConversions.propertiesAsScalaMap

object Main {

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
        contentfulUrl = Url("http://www.disney.com"),
        jdbcConfig = JdbcConfig(
          Uri(s"jdbc:hsqldb:mem:local"),
          "sa",
          "",
          "SELECT 1"
        )
      ),

      localPort = Port(80)
    )
  }
}
