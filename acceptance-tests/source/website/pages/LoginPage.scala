package uk.org.lidalia.exampleapp.acceptancetests.website.pages

import uk.org.lidalia.net.Path
import uk.org.lidalia.webdriver.{Page, PageFactory, ReusableWebDriver}

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
