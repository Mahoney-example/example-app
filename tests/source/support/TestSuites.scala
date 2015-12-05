package uk.org.lidalia
package exampleapp.tests
package support

import ch.qos.logback.classic.Level
import org.scalatest.{Args, Status, Suite, Suites}
import uk.org.lidalia.exampleapp.system.logging.JulConfigurer.sendJulToSlf4j
import uk.org.lidalia.exampleapp.system.logging.LogbackLoggingDefinition
import uk.org.lidalia.scalalang.ResourceFactory

abstract class TestSuites[R](
  optionFactory: ?[ResourceFactory[R]]
) extends Suite {

  override def run(testName: Option[String], args: Args): Status = {

    if (optionFactory.isDefined) {

      runSuites(testName, args, optionFactory.get)

    } else {

      sendJulToSlf4j()

      LogbackLoggingDefinition(logLevels).using { () =>

        metaFactory.using { factory =>
          runSuites(testName, args, factory)
        }
      }
    }
  }

  private def runSuites(testName: Option[String], args: Args, factory: ResourceFactory[R]): Status = {
    val suites = nestedSuites(factory)
    new Suites(suites: _*).run(testName, args)
  }

  def nestedSuites(factory: ResourceFactory[R]): List[Suite]

  def metaFactory: ResourceFactory[ResourceFactory[R]]

  def logLevels: List[(String, Level)] = List()
}
