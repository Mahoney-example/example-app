package uk.org.lidalia
package exampleapp
package tests
package support

import org.scalatest.{ConfigMap, Suite}
import uk.org.lidalia.exampleapp.local.{Environment, EnvironmentDefinition}
import uk.org.lidalia.exampleapp.system.logging.LogbackLoggingDefinition
import uk.org.lidalia.exampleapp.tests.library.{ReusableWebDriver, WebDriverDefinition}
import uk.org.lidalia.scalalang.{PoolFactory, ResourceFactory}

class BrowserFunctionalTestsRunner(
  suiteBuilder: (ResourceFactory[Environment], ResourceFactory[ReusableWebDriver]) => Suite
) extends App {
  LogbackLoggingDefinition().using { loggerFactory =>
    ResourceFactory.usingAll(
      PoolFactory(EnvironmentDefinition(loggerFactory = loggerFactory)),
      PoolFactory(WebDriverDefinition())
    ) { (envFactory, webDriverFactory) =>
      suiteBuilder(
        envFactory,
        webDriverFactory
      ).execute(durations = true, fullstacks = true, stats = true)
    }
  }
}
