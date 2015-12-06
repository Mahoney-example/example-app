package uk.org.lidalia
package exampleapp.tests

import ch.qos.logback.classic.Level.INFO
import org.scalatest.Suites
import scalalang.PoolFactory
import support.{LoggingConfiguredSuite, FixtureProvider, TestEnvironment, TestEnvironmentDefinition}

class FunctionalTests extends Suites(
   new LoginTests,
   new RegisterTests
)
with FixtureProvider[TestEnvironment]
with LoggingConfiguredSuite
{

  override def metaFactory = PoolFactory(TestEnvironmentDefinition())

  override def logLevels = List(
    "uk.org.lidalia" -> INFO
  )
}
