package uk.org.lidalia
package exampleapp.server

import ch.qos.logback.classic.Level
import uk.org.lidalia.exampleapp.server.application.ApplicationDefinition
import uk.org.lidalia.exampleapp.system.logging.LogbackLoggingDefinition

import collection.JavaConversions.mapAsScalaMap
import collection.JavaConversions.propertiesAsScalaMap

object Main {

  def main(args: Array[String]) {

    LogbackLoggingDefinition(
      "uk.org.lidalia.exampleapp" -> Level.INFO
    ).using { loggerFactory =>

      try {

        val config = Configuration(
          args.toVector,
          System.getProperties.toMap,
          System.getenv().toMap
        )

        val app = ApplicationDefinition(config.applicationConfig, loggerFactory)

        ProcessDefinition(app, config)
          .runUntilShutdown()

      } catch {
        case e: Throwable => loggerFactory.getLogger(getClass).error("Unexpected exception running server", e)
      }
    }
  }
}
