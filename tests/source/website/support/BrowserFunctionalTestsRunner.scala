package uk.org.lidalia
package exampleapp
package tests
package website
package support

import library.webdriver.ReusableWebDriver
import org.scalatest.Suite
import uk.org.lidalia.exampleapp.local.Environment
import uk.org.lidalia.exampleapp.tests.support.FunctionalTestEnvironmentFactory
import uk.org.lidalia.scalalang.ResourceFactory

class BrowserFunctionalTestsRunner(
  suiteBuilder: (ResourceFactory[Environment], ResourceFactory[ReusableWebDriver]) => Suite
) extends App {

  FunctionalTestEnvironmentFactory().using { factories =>
    suiteBuilder(
      factories.environmentFactory,
      factories.webDriverFactory
    ).execute(durations = true, fullstacks = true, stats = true)
  }
}
