package uk.org.lidalia
package exampleapp.tests.support

import ch.qos.logback.classic.Level
import org.scalatest.{Args, ConfigMap, Status, Suite}
import uk.org.lidalia.exampleapp.system.logging.JulConfigurer.sendJulToSlf4j
import uk.org.lidalia.exampleapp.system.logging.LogbackLoggingDefinition
import uk.org.lidalia.scalalang.ResourceFactory

trait FixtureProvider[R] extends Suite {

  type FixtureParam = R

  override def run(testName: Option[String], args: Args): Status = {

    if (args.configMap.contains("fixtureFactory")) {
      super.run(testName, args)
    } else {

      sendJulToSlf4j()
      LogbackLoggingDefinition(logLevels).using { () =>

        metaFactory.using { factory =>
          super.run(testName, args.copy(configMap = ConfigMap("fixtureFactory" -> factory)))
        }
      }
    }
  }

  def metaFactory: ResourceFactory[ResourceFactory[R]]

  def logLevels: List[(String, Level)] = List()
}
