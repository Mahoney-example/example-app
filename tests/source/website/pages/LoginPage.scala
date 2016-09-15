package uk.org.lidalia
package exampleapp.tests
package website.pages

import library.webdriver.{Page, PageFactory, ReusableWebDriver}
import uk.org.lidalia.net.Path

object LoginPage extends PageFactory[LoginPage] {

  override val url = Path("/login")

  override def apply(reusableWebDriver: ReusableWebDriver): LoginPage = new LoginPage(reusableWebDriver)
}

class LoginPage(val driver: ReusableWebDriver) extends Page[LoginPage] {

  lazy val loginButton = $("button.login")

  override def isCurrentPage: Boolean = {
    title == "Login Page"
  }
}
