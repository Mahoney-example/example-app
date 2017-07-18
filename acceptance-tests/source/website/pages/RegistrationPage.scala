package uk.org.lidalia.exampleapp.acceptancetests.website.pages

import uk.org.lidalia.net.Path
import uk.org.lidalia.webdriver.{Page, PageFactory, ReusableWebDriver}

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
