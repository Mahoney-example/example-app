package uk.org.lidalia.exampleapp.acceptancetests.website.support

import org.scalatest.Suite
import uk.org.lidalia.exampleapp.local.Environment
import uk.org.lidalia.exampleapp.acceptancetests.support.FunctionalTestEnvironmentFactory
import uk.org.lidalia.scalalang.ResourceFactory
import uk.org.lidalia.webdriver.ReusableWebDriver

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
