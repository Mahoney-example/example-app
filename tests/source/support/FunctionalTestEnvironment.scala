package uk.org.lidalia
package exampleapp
package tests
package support

import uk.org.lidalia.exampleapp.local.{Environment, EnvironmentDefinition}
import uk.org.lidalia.exampleapp.system.logging.LogbackLoggingDefinition
import uk.org.lidalia.exampleapp.tests.library.{ReusableWebDriver, WebDriverDefinition}
import uk.org.lidalia.scalalang.{PoolFactory, ResourceFactory}

object FunctionalTestEnvironment {
  def apply(): FunctionalTestEnvironment = new FunctionalTestEnvironment()
}

class FunctionalTestEnvironment private ()
  extends ResourceFactory[(ResourceFactory[Environment], ResourceFactory[ReusableWebDriver])] {

  override def using[T](work: ((ResourceFactory[Environment], ResourceFactory[ReusableWebDriver])) => T): T = {
    LogbackLoggingDefinition().using { loggerFactory =>
      ResourceFactory.usingAll(
        PoolFactory(EnvironmentDefinition(loggerFactory = loggerFactory)),
        PoolFactory(WebDriverDefinition())
      ) { (envFactory, webDriverFactory) =>
        work((envFactory, webDriverFactory))
      }
    }
  }
}
