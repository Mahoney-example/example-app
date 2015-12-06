package uk.org.lidalia
package exampleapp.tests.support

import ch.qos.logback.classic.Level
import org.scalatest.{Args, Status, Suite}
import uk.org.lidalia.exampleapp.system.logging.JulConfigurer.sendJulToSlf4j
import uk.org.lidalia.exampleapp.system.logging.LogbackLoggingDefinition

trait LoggingConfiguredSuite extends Suite {

  protected val logLevels: List[(String, Level)] = List()

  override def run(testName: Option[String], args: Args): Status = {

    if (args.configMap.contains("loggerContext")) {
      super.run(testName, args)
    } else {

      sendJulToSlf4j()
      LogbackLoggingDefinition(logLevels).using { loggerContext =>
        super.run(testName, args.copy(configMap = args.configMap.+("loggerContext" -> loggerContext)))
      }
    }
  }
}
