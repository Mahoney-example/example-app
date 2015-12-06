package uk.org.lidalia
package exampleapp.tests.support

import ch.qos.logback.classic.Level.INFO
import org.scalatest.fixture
import scalalang.{PoolFactory, ResourceFactory}

abstract class EnvironmentTests
extends fixture.FunSuite
with FixtureProvider[TestEnvironment]
with LoggingConfiguredSuite
{

  override protected def withFixture(test: OneArgTest) = {

    val envDefinition = test.configMap.getRequired[ResourceFactory[TestEnvironment]]("fixtureFactory")

    envDefinition.using { environment =>
      test.apply(environment)
    }
  }

  override def metaFactory = PoolFactory(TestEnvironmentDefinition())

  override def logLevels = List(
    "uk.org.lidalia" -> INFO
  )
}
