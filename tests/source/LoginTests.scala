package uk.org.lidalia
package exampleapp
package tests

import pages.{LoggedInPage, LoginPage}
import local.{Environment, EnvironmentDefinition}
import library.{ReusableWebDriver, WebDriverDefinition}
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
    user("Joe").exists()

    // when
    val loginPage = browser.to(LoginPage)
    loginPage.loginButton.click()

    // then
    val loggedInPage = browser.at(LoggedInPage)
    assert(
      loggedInPage.welcomeMessage.contains("Joe")
    )
  }

  def this() = this(EnvironmentDefinition(), WebDriverDefinition())

  def user(username: String): User = new User(username)

  case class User(name: String) {
    def exists() = Unit
  }
}
