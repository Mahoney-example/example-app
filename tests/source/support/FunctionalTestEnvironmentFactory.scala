package uk.org.lidalia
package exampleapp
package tests
package support

import ch.qos.logback.classic.Level
import uk.org.lidalia.exampleapp.local.{Environment, EnvironmentDefinition}
import uk.org.lidalia.exampleapp.system.logging.{LogbackLoggerFactory, LogbackLoggingDefinition}
import uk.org.lidalia.exampleapp.tests.library.{ReusableWebDriver, WebDriverDefinition}
import uk.org.lidalia.scalalang.{Pool, PoolFactory, ResourceFactory}

object FunctionalTestEnvironmentFactory {
  def apply(): FunctionalTestEnvironmentFactory = new FunctionalTestEnvironmentFactory()
}

class FunctionalTestEnvironmentFactory private()
  extends ResourceFactory[FunctionalTestEnvironment] {

  override def using[T](work: FunctionalTestEnvironment => T): T = {
    LogbackLoggingDefinition("uk.org.lidalia" -> Level.INFO).using { loggerFactory =>
      ResourceFactory.usingAll(
        PoolFactory(EnvironmentDefinition(loggerFactory = loggerFactory)),
        PoolFactory(WebDriverDefinition())
      ) { (envFactory, webDriverFactory) =>
        work(FunctionalTestEnvironment(loggerFactory, envFactory, webDriverFactory))
      }
    }
  }
}

case class FunctionalTestEnvironment(
  loggerFactory: LogbackLoggerFactory,
  environmentFactory: Pool[Environment],
  webDriverFactory: Pool[ReusableWebDriver]
)
