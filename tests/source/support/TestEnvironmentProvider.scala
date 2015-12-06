package uk.org.lidalia
package exampleapp.tests.support

import ch.qos.logback.classic.Level.INFO
import uk.org.lidalia.scalalang.PoolFactory

trait TestEnvironmentProvider
extends FixtureProvider[TestEnvironment]
with LoggingConfiguredSuite
{
  override protected val metaFactory = PoolFactory(TestEnvironmentDefinition())

  override protected val logLevels = List(
    "uk.org.lidalia" -> INFO
  )
}
