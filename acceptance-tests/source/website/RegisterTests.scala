package uk.org.lidalia.exampleapp.acceptancetests.website

import uk.org.lidalia.exampleapp.local.{Environment, EnvironmentDefinition}
import uk.org.lidalia.webdriver.{ReusableWebDriver, WebDriverDefinition}
import uk.org.lidalia.exampleapp.acceptancetests.website.support.BrowserFunctionalTests
import uk.org.lidalia.scalalang.ResourceFactory
import uk.org.lidalia.exampleapp.acceptancetests.website.pages.{LoggedInPage, RegistrationPage}

class RegisterTests(
  envFactory: ResourceFactory[Environment],
  webDriverFactory: ResourceFactory[ReusableWebDriver]
) extends BrowserFunctionalTests(
  envFactory,
  webDriverFactory
) {

  test("can register a new user") { args =>
    val (env, browser) = args

    // given
    val registrationPage = browser.to(RegistrationPage)

    // when
    registrationPage.registerButton.click()

    // then
    val loggedInPage = browser.at(LoggedInPage)

    assert(
      loggedInPage.welcomeMessage.contains("Joe")
    )

  }

  def this() = this(EnvironmentDefinition(), WebDriverDefinition())

}
