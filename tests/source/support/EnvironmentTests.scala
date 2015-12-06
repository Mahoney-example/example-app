package uk.org.lidalia
package exampleapp.tests.support

import org.scalatest.fixture
import scalalang.{PoolFactory, ResourceFactory}

abstract class EnvironmentTests
extends fixture.FunSuite
with FixtureProvider[TestEnvironment] {

  override protected def withFixture(test: OneArgTest) = {

    val envDefinition = test.configMap.getRequired[ResourceFactory[TestEnvironment]]("fixtureFactory")

    envDefinition.using { environment =>
      test.apply(environment)
    }
  }

  override def metaFactory = PoolFactory(TestEnvironmentDefinition())
}
