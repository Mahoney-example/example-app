package uk.org.lidalia
package exampleapp.tests
package support

import ch.qos.logback.classic.Level
import org.scalatest.{Args, Status, Suite, Suites}
import uk.org.lidalia.exampleapp.system.logging.JulConfigurer.sendJulToSlf4j
import uk.org.lidalia.exampleapp.system.logging.LogbackLoggingDefinition
import uk.org.lidalia.scalalang.ResourceFactory

abstract class TestSuite[R](
  envDefinition: ?[ResourceFactory[R]]
) extends Suite {

  def this() = this(None)

  def nestedTests(factory: ResourceFactory[R]): List[Suite]
  def metaFactory: ResourceFactory[ResourceFactory[R]]

  override def run(testName: Option[String], args: Args): Status = {

    if (envDefinition.isDefined) {
      val suites = nestedTests(envDefinition.get)
      new Suites(suites:_*).run(testName, args)
    } else {
      sendJulToSlf4j()

      val loggingDefinition = LogbackLoggingDefinition(
        "uk.org.lidalia" -> Level.INFO
      )

      loggingDefinition.using { () =>

        metaFactory.using { factory =>

          val suites = nestedTests(factory)
          new Suites(suites:_*).run(testName, args)

        }
      }
    }
  }
}
