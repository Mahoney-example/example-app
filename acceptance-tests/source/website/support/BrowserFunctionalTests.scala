package uk.org.lidalia.exampleapp.acceptancetests.website.support

import org.scalatest.{Outcome, fixture}
import uk.org.lidalia.exampleapp.local.Environment
import uk.org.lidalia.scalalang.ResourceFactory
import uk.org.lidalia.scalalang.ResourceFactory.usingAll
import uk.org.lidalia.webdriver.{ReusableWebDriver, WebDriverWithBaseUrl}

abstract class BrowserFunctionalTests(
  envFactory: ResourceFactory[Environment],
  webDriverFactory: ResourceFactory[ReusableWebDriver]
) extends fixture.FunSuite {

  override type FixtureParam = (Environment, WebDriverWithBaseUrl)

  override protected def withFixture(test: OneArgTest): Outcome = {
    usingAll(
      envFactory,
      webDriverFactory
    ) { (env, driver) =>
      test.apply((env, WebDriverWithBaseUrl(driver, env.servers.head.localPort)))
    }
  }
}
