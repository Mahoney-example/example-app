package uk.org.lidalia
package exampleapp.tests
package website.pages

import library.webdriver.{Page, PageFactory, ReusableWebDriver}
import org.openqa.selenium.By
import uk.org.lidalia.net.Path

object LoginPage extends PageFactory[LoginPage] {

  override val url = Path("/login")

  override def apply(reusableWebDriver: ReusableWebDriver): LoginPage = new LoginPage(reusableWebDriver)
}

class LoginPage(val driver: ReusableWebDriver) extends Page[LoginPage] {

  val loginButton = driver.findElement(By.cssSelector("button.login"))

  override def isCurrentPage: Boolean = {
    title == "Login Page"
  }
}
