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

  def this() = this(None)

  override def run(testName: Option[String], args: Args): Status = {

    if (optionFactory.isDefined) {

      val suites = nestedSuites(optionFactory.get)
      new Suites(suites:_*).run(testName, args)

    } else {

      sendJulToSlf4j()

      val loggingDefinition = LogbackLoggingDefinition(
        "uk.org.lidalia" -> Level.INFO
      )

      loggingDefinition.using { () =>

        metaFactory.using { factory =>

          val suites = nestedSuites(factory)
          new Suites(suites:_*).run(testName, args)

        }
      }
    }
  }

  def nestedSuites(factory: ResourceFactory[R]): List[Suite]

  def metaFactory: ResourceFactory[ResourceFactory[R]]
}
