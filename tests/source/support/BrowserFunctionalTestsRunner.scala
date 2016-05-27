package uk.org.lidalia
package exampleapp
package tests
package support

import org.scalatest.Suite
import uk.org.lidalia.exampleapp.local.Environment
import uk.org.lidalia.exampleapp.tests.library.ReusableWebDriver
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
