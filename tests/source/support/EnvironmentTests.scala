package uk.org.lidalia
package exampleapp.tests.support

import ch.qos.logback.classic.Level.INFO
import org.scalatest.fixture
import scalalang.PoolFactory

abstract class EnvironmentTests
extends fixture.FunSuite
with FixtureSuite[TestEnvironment]
with LoggingConfiguredSuite
{

  override protected val metaFactory = PoolFactory(TestEnvironmentDefinition())

  override protected val logLevels = List(
    "uk.org.lidalia" -> INFO
  )
}
