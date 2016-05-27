package uk.org.lidalia
package exampleapp.tests

import ch.qos.logback.classic.Level.INFO
import org.scalatest.{Args, Status, Suite, Suites}
import uk.org.lidalia.exampleapp.local.{Environment, EnvironmentDefinition}
import uk.org.lidalia.exampleapp.system.logging.LogbackLoggingDefinition
import uk.org.lidalia.exampleapp.tests.library.{ReusableWebDriver, WebDriverDefinition}
import uk.org.lidalia.scalalang.{PoolFactory, ResourceFactory}

class FunctionalTestSuites extends Suite {

  def suites(
    envFactory: ResourceFactory[Environment],
    webDriverFactory: ResourceFactory[ReusableWebDriver]
  ) = List(
    new LoginTests(envFactory, webDriverFactory),
    new RegisterTests(envFactory, webDriverFactory)
  )

  override def run(testName: Option[String], args: Args): Status = {
    LogbackLoggingDefinition("uk.org.lidalia" -> INFO).using { loggerFactory =>
      ResourceFactory.usingAll(
        PoolFactory(EnvironmentDefinition(loggerFactory = loggerFactory)),
        PoolFactory(WebDriverDefinition())
      ) { (envFactory, webDriverFactory) =>
        new Suites(suites(envFactory, webDriverFactory):_*).run(testName, args)
      }
    }
  }
}
