package uk.org.lidalia
package exampleapp.tests

import library.{Page, PageFactory, ReusableWebDriver, WebDriverWithBaseUrl}
import uk.org.lidalia.exampleapp.local.Environment
import uk.org.lidalia.exampleapp.tests.pages.LoginPage
import uk.org.lidalia.exampleapp.tests.support.BrowserFunctionalTests
import uk.org.lidalia.net.Scheme.http
import uk.org.lidalia.net._

class LoginTests extends BrowserFunctionalTests {

    test("can login via the browser") { case (env, browser) =>


      // given
//        user("Joe").existsInSystem()
      // when
        browser.to(LoginPage)
      // then
    }
}
