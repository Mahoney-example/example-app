package uk.org.lidalia
package exampleapp.tests.support

import org.scalatest.{Outcome, fixture}
import uk.org.lidalia.exampleapp.local.Environment
import uk.org.lidalia.exampleapp.tests.library.{LogbackLoggerFactoryPerRun, ReusableWebDriver, WebDriverDefinitionPerRun, WebDriverWithBaseUrl}
import uk.org.lidalia.scalalang.ResourceFactory.usingAll

trait BrowserFunctionalTests
extends fixture.FunSuite
with EnvironmentDefinitionPerRun
with WebDriverDefinitionPerRun
with LogbackLoggerFactoryPerRun {

  override type FixtureParam = (Environment, WebDriverWithBaseUrl)

  override protected def withFixture(test: OneArgTest): Outcome = {
    usingAll(
      environmentDefinition(test.configMap),
      webDriverDefinition(test.configMap)
    ) { (env, driver) =>
      test.apply((env, WebDriverWithBaseUrl(driver, env.servers.head.localPort)))
    }
  }
}
