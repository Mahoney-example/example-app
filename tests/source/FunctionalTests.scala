package uk.org.lidalia.exampleapp.tests

import ch.qos.logback.classic.Level
import org.scalatest._
import uk.org.lidalia.exampleapp.system.logging.JulConfigurer.sendJulToSlf4j
import uk.org.lidalia.exampleapp.system.logging.LogbackLoggingDefinition
import uk.org.lidalia.exampleapp.tests.functional.RegisterTests
import uk.org.lidalia.exampleapp.tests.support.TestEnvironmentDefinition
import uk.org.lidalia.scalalang.PoolFactory

class FunctionalTests extends Suite {

  private var actualNestedSuites: collection.immutable.IndexedSeq[Suite] = null

  override def run(testName: Option[String], args: Args): Status = {

    sendJulToSlf4j()

    val loggingDefinition = LogbackLoggingDefinition(
      "uk.org.lidalia" -> Level.INFO
    )
    val envFactory = PoolFactory(TestEnvironmentDefinition())

    loggingDefinition.using { () =>

      envFactory.using { pool =>

        val suites = new Suites(
          new RegisterTests(pool),
          new RegisterTests(pool)
        )

        suites.run(testName, args)

      }
    }
  }
}
