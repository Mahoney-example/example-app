package uk.org.lidalia
package exampleapp.tests.support

import ch.qos.logback.classic.Level
import org.scalatest.{ConfigMap, Status, Args, fixture}
import uk.org.lidalia.exampleapp.system.logging.JulConfigurer.sendJulToSlf4j
import uk.org.lidalia.exampleapp.system.logging.LogbackLoggingDefinition
import uk.org.lidalia.scalalang.ResourceFactory

trait TestClass[R] extends fixture.FunSuite {

  override type FixtureParam = R

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

  override protected def withFixture(test: OneArgTest) = {
    val fixture = test.configMap.getRequired[ResourceFactory[R]]("fixtureFactory")
    fixture.using { environment =>
      test.apply(environment)
    }
  }

  def metaFactory: ResourceFactory[ResourceFactory[R]]

  def logLevels: List[(String, Level)] = List()
}
