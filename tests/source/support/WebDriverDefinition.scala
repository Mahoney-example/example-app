package uk.org.lidalia.exampleapp.tests.support

import java.io.File

import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeDriverService.Builder
import org.slf4j.LoggerFactory
import uk.org.lidalia.exampleapp.system.HasLogger
import uk.org.lidalia.scalalang.ResourceFactory
import uk.org.lidalia.scalalang.ResourceFactory._try

object WebDriverDefinition {

  def apply() = new WebDriverDefinition

  val log = LoggerFactory.getLogger(classOf[WebDriverDefinition])
}

class WebDriverDefinition private() extends ResourceFactory[ReusableWebDriver] with HasLogger {

  override def using[T](work: (ReusableWebDriver) => T): T = {

    val driverFile: File = findChromeDriver
    val chromeDriverService = new Builder().usingDriverExecutable(driverFile).build()

    _try {
      log.info("Starting web driver")
      chromeDriverService.start()
      log.info("Started web driver")
      work(ReusableWebDriver(new ChromeDriver(chromeDriverService)))
    } _finally {
      log.info("Stopping web driver")
      chromeDriverService.stop()
      log.info("Stopped web driver")
    }
  }

  def findChromeDriver: File = {
    val driverUrl = Thread.currentThread().getContextClassLoader.getResource("chromedriver-mac")
    new File(driverUrl.toURI)
  }
}
