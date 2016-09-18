package uk.org.lidalia
package exampleapp
package tests.website

import local.{Environment, EnvironmentDefinition}
import tests.library.webdriver.{ReusableWebDriver, WebDriverDefinition}
import support.BrowserFunctionalTests
import scalalang.ResourceFactory
import pages.{LoggedInPage, RegistrationPage}

class RegisterTests(
  envFactory: ResourceFactory[Environment],
  webDriverFactory: ResourceFactory[ReusableWebDriver]
) extends BrowserFunctionalTests(
  envFactory,
  webDriverFactory
) {

  test("can register a new user") { case (env, browser) =>

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
