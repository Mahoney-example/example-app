package uk.org.lidalia
package exampleapp.server

import java.lang.System.{getProperties, getenv}

import ch.qos.logback.classic.Level
import uk.org.lidalia.exampleapp.system.logging.LogbackLoggingDefinition

import collection.JavaConversions.mapAsScalaMap
import collection.JavaConversions.propertiesAsScalaMap

object Main {

  def main(args: Array[String]) {

    LogbackLoggingDefinition(
      "uk.org.lidalia.exampleapp" -> Level.INFO
    ).using { loggerFactory =>

      val config = Configuration(
        args.toVector,
        getProperties.toMap,
        getenv().toMap
      )

      ServerDefinition(loggerFactory, config).runUntilShutdown()

    }
  }
}
