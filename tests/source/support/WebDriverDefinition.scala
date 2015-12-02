package uk.org.lidalia.exampleapp.tests.support

import java.io.File

import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeDriverService.Builder
import uk.org.lidalia.scalalang.ResourceFactory
import uk.org.lidalia.scalalang.ResourceFactory._try

object WebDriverDefinition {

  def apply() = new WebDriverDefinition
}

class WebDriverDefinition private() extends ResourceFactory[ReusableWebDriver] {

  override def using[T](work: (ReusableWebDriver) => T): T = {

    val driverFile: File = findChromeDriver
    val chromeDriverService = new Builder().usingDriverExecutable(driverFile).build()

    _try {
      chromeDriverService.start()
      work(ReusableWebDriver(new ChromeDriver(chromeDriverService)))
    } _finally {
      chromeDriverService.stop()
    }
  }

  def findChromeDriver: File = {
    val driverUrl = Thread.currentThread().getContextClassLoader.getResource("chromedriver-mac")
    new File(driverUrl.toURI)
  }
}
