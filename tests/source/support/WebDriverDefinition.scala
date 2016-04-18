package uk.org.lidalia
package exampleapp.tests.support

import java.io.{File, FileOutputStream}
import java.net.URL
import java.nio.file.Files

import org.apache.commons.io.IOUtils
import org.apache.commons.io.IOUtils.copy
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeDriverService.Builder
import org.slf4j.LoggerFactory
import uk.org.lidalia.exampleapp.system.HasLogger
import uk.org.lidalia.exampleapp.system.display.Display
import uk.org.lidalia.exampleapp.tests.support.WebDriverDefinition.driverFile
import uk.org.lidalia.scalalang.ResourceFactory
import uk.org.lidalia.scalalang.ResourceFactory._try
import uk.org.lidalia.scalalang.os.OsFamily

import collection.JavaConversions.mapAsJavaMap

object WebDriverDefinition {

  def apply(display: ?[Display] = None) = new WebDriverDefinition(display)

  val log = LoggerFactory.getLogger(classOf[WebDriverDefinition])

  def driverFile(os: OsFamily): File = {
    val fileName = s"chromedriver-${os.toString.toLowerCase}"
    getResourceAsFile(fileName)
  }

  def getResourceAsFile(fileName: String): File = {
    val url = Thread.currentThread().getContextClassLoader.getResource(fileName)
    try {
      new File(url.toURI)
    } catch {
      case e: Exception =>
        val in = url.openStream()
        val tempFile = File.createTempFile(String.valueOf(in.hashCode()), ".tmp")
        tempFile.deleteOnExit()
        Files.copy(in, tempFile.toPath)
        tempFile
    }
  }

  def main(args: Array[String]) {
    WebDriverDefinition().using { driver =>
      println(s"Using $driver")
    }
  }
}

class WebDriverDefinition private(display: ?[Display]) extends ResourceFactory[ReusableWebDriver] with HasLogger {

  override def using[T](work: (ReusableWebDriver) => T): T = {

    val env = display.map { d => Map("DISPLAY" -> s":${d.id}") }.getOrElse(Map())
    val chromeDriver = driverFile(OsFamily())
    val chromeDriverService = new Builder()
        .usingDriverExecutable(chromeDriver)
        .withSilent(true)
        .withEnvironment(env)
        .build()

    _try {
      log.info("Starting web driver")
      chromeDriverService.start()

      val driver = ReusableWebDriver(new ChromeDriver(chromeDriverService))
      log.info("Started web driver")

      work(driver)

    } _finally {
      log.info("Stopping web driver")
      chromeDriverService.stop()
      log.info("Stopped web driver")
    }
  }
}
