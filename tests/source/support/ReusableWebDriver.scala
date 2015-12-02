package uk.org.lidalia.exampleapp.tests.support

import org.openqa.selenium.{By, WebDriver}
import uk.org.lidalia.scalalang.Reusable

object ReusableWebDriver {
  def apply(delegate: WebDriver) = new ReusableWebDriver(delegate)
}

class ReusableWebDriver private (delegate: WebDriver) extends WebDriver with Reusable {

  override def getPageSource = delegate.getPageSource

  override def findElements(by: By) = delegate.findElements(by)

  override def getWindowHandle = delegate.getWindowHandle

  override def get(url: String) = delegate.get(url)

  override def manage() = delegate.manage()

  override def getWindowHandles = delegate.getWindowHandles

  override def quit() = throw new UnsupportedOperationException(s"Closing is handled by ${classOf[WebDriverDefinition].getSimpleName}")

  override def switchTo() = delegate.switchTo()

  override def close() = throw new UnsupportedOperationException(s"Closing is handled by ${classOf[WebDriverDefinition].getSimpleName}")

  override def getCurrentUrl = delegate.getCurrentUrl

  override def navigate() = delegate.navigate()

  override def findElement(by: By) = delegate.findElement(by)

  override def getTitle = delegate.getTitle

  override def reset() = manage().deleteAllCookies()
}
