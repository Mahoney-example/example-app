package uk.org.lidalia.exampleapp.tests.library

import org.scalatest.ConfigMap
import uk.org.lidalia.exampleapp.tests.support.{ReusableWebDriver, WebDriverDefinition}
import uk.org.lidalia.scalalang.PoolFactory

trait WebDriverDefinitionPerRun extends OnePerRun {

  private val key = "WebDriverDefinition"

  register(key, PoolFactory(WebDriverDefinition()))

  def webDriverDefinition(configMap: ConfigMap) = {
    getResourceFactory[ReusableWebDriver](configMap, key)
  }
}
