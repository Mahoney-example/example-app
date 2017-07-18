package uk.org.lidalia.exampleapp.acceptancetests.website.pages

import org.openqa.selenium.By
import uk.org.lidalia.net.Path
import uk.org.lidalia.webdriver.{Page, PageFactory, ReusableWebDriver}

object LoggedInPage extends PageFactory[LoggedInPage] {

  override val url = Path("/login")

  override def apply(reusableWebDriver: ReusableWebDriver): LoggedInPage = new LoggedInPage(reusableWebDriver)
}

class LoggedInPage(val driver: ReusableWebDriver) extends Page[LoggedInPage] {

  val welcomeMessage = driver.findElement(By.tagName("h1")).getText

  override def isCurrentPage: Boolean = title.contains("Welcome ")

}
