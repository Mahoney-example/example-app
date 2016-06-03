package uk.org.lidalia
package exampleapp.tests.website

import org.slf4j.LoggerFactory
import uk.org.lidalia.exampleapp.local.{Environment, EnvironmentDefinition}
import uk.org.lidalia.exampleapp.tests.library.webdriver.{ReusableWebDriver, WebDriverDefinition}
import uk.org.lidalia.exampleapp.tests.website.support.BrowserFunctionalTests
import uk.org.lidalia.scalalang.ResourceFactory

class RegisterTests(
  envFactory: ResourceFactory[Environment],
  webDriverFactory: ResourceFactory[ReusableWebDriver]
) extends BrowserFunctionalTests(
  envFactory,
  webDriverFactory
) {

  test("register") { environment =>
    LoggerFactory.getLogger(classOf[RegisterTests]).info("Test in progress")
  }

  test("register2") { environment =>
    LoggerFactory.getLogger(classOf[RegisterTests]).info("Test in progress")
  }

  def this() = this(EnvironmentDefinition(), WebDriverDefinition())
}
