package uk.org.lidalia.exampleapp.acceptancetests.website

import uk.org.lidalia.exampleapp.acceptancetests.website.pages.{LoggedInPage, LoginPage}
import uk.org.lidalia.exampleapp.acceptancetests.website.support.BrowserFunctionalTests
import uk.org.lidalia.exampleapp.local.{Environment, EnvironmentDefinition}
import uk.org.lidalia.exampleapp.tests.dsl.User
import uk.org.lidalia.scalalang.ResourceFactory
import uk.org.lidalia.webdriver.{ReusableWebDriver, WebDriverDefinition}

class LoginTests(
  envFactory: ResourceFactory[Environment],
  webDriverFactory: ResourceFactory[ReusableWebDriver]
) extends BrowserFunctionalTests(
  envFactory,
  webDriverFactory
) {

  test("can login via the browser") { args =>
    val (env, browser) = args

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
