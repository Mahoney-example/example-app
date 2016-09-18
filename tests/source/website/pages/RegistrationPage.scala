package uk.org.lidalia
package exampleapp.tests
package website.pages

import uk.org.lidalia.exampleapp.tests.library.webdriver.{Page, PageFactory, ReusableWebDriver}
import uk.org.lidalia.net.Path

object RegistrationPage extends PageFactory[RegistrationPage] {

  override val url = Path("/register")

  override def apply(reusableWebDriver: ReusableWebDriver) = new RegistrationPage(reusableWebDriver)
}

class RegistrationPage(val driver: ReusableWebDriver) extends Page[RegistrationPage] {

  lazy val registerButton = $("button.register")

  override def isCurrentPage: Boolean = {
    title == "Registration Page"
  }
}
