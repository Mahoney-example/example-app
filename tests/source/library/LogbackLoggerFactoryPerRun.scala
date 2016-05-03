package uk.org.lidalia.exampleapp.tests.library

import ch.qos.logback.classic.Level.INFO
import org.scalatest.ConfigMap
import uk.org.lidalia.exampleapp.system.logging.{LogbackLoggerFactory, LogbackLoggingDefinition}

trait LogbackLoggerFactoryPerRun extends OnePerRun {

  private val key = "LoggerContext"

  register(key, LogbackLoggingDefinition(
    "uk.org.lidalia" -> INFO
  ))

  def loggerContext(configMap: ConfigMap) = {
    getResourceFactory[LogbackLoggerFactory](configMap, key)
  }
}
