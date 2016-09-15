package uk.org.lidalia
package exampleapp
package tests.website

import tests.library.webdriver.{ReusableWebDriver, WebDriverDefinition}
import tests.dsl.User
import pages.{LoggedInPage, LoginPage}
import local.{Environment, EnvironmentDefinition}
import support.BrowserFunctionalTests
import scalalang.ResourceFactory

class LoginTests(
  envFactory: ResourceFactory[Environment],
  webDriverFactory: ResourceFactory[ReusableWebDriver]
) extends BrowserFunctionalTests(
  envFactory,
  webDriverFactory
) {

  test("can login via the browser") { case (env, browser) =>

    // given
    User("Joe").exists()
    val loginPage = browser.to(LoginPage)

    // when
    loginPage.loginButton.click()

    // then
    val loggedInPage = browser.at(LoggedInPage)
    assert(
      loggedInPage.welcomeMessage.contains("Joe")
    )
  }

  def this() = this(EnvironmentDefinition(), WebDriverDefinition())

}
