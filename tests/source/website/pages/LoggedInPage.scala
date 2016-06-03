package uk.org.lidalia
package exampleapp.tests
package website.pages

import org.openqa.selenium.By
import uk.org.lidalia.exampleapp.tests.library.webdriver.{Page, PageFactory, ReusableWebDriver}
import uk.org.lidalia.net.Path

object LoggedInPage extends PageFactory[LoggedInPage] {

  override val url = Path("/login")

  override def apply(reusableWebDriver: ReusableWebDriver): LoggedInPage = new LoggedInPage(reusableWebDriver)
}

class LoggedInPage(val driver: ReusableWebDriver) extends Page[LoggedInPage] {

  val welcomeMessage = driver.findElement(By.tagName("h1")).getText

  override def isCurrentPage: Boolean = title.contains("Welcome ")

}
