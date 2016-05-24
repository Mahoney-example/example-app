package uk.org.lidalia
package exampleapp.tests

import java.time.Instant

import pages.{LoggedInPage, LoginPage}
import uk.org.lidalia.exampleapp.tests.support.BrowserFunctionalTests

class LoginTests extends BrowserFunctionalTests {

  test("can login via the browser") { case (env, browser) =>

    println(s"${Instant.now()} Test start")

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

  def user(username: String): User = new User(username)

  case class User(name: String) {
    def exists() = Unit
  }
}
