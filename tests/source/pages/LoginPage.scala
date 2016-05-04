package uk.org.lidalia
package exampleapp.tests
package pages

import library.{Page, PageFactory}
import uk.org.lidalia.exampleapp.tests.library.ReusableWebDriver
import uk.org.lidalia.net.UriReference

object LoginPage extends PageFactory[LoginPage] {

  override val url: UriReference = "/login"

  override def apply(reusableWebDriver: ReusableWebDriver): LoginPage = new LoginPage(reusableWebDriver)
}

class LoginPage(webDriver: ReusableWebDriver) extends Page[LoginPage]
