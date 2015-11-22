package uk.org.lidalia
package exampleapp

import ch.qos.logback.classic.Level
import server.web.ServerDefinition
import system.logging.LogbackLoggingDefinition

import collection.JavaConversions.mapAsScalaMap
import collection.JavaConversions.propertiesAsScalaMap

object Main {

  def main(args: Array[String]) {

    LogbackLoggingDefinition(
      "uk.org.lidalia.exampleapp" -> Level.INFO
    ).using { () =>

      val config = Configuration(
        args.toVector,
        System.getProperties.toMap,
        System.getenv().toMap
      )

      val server = ServerDefinition(config)

      server.runUntilShutdown()
    }
  }
}
