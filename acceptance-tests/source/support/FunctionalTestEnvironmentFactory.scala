package uk.org.lidalia.exampleapp.acceptancetests.support

import ch.qos.logback.classic.Level
import org.slf4j.Logger
import uk.org.lidalia.exampleapp.local.{Environment, EnvironmentDefinition}
import uk.org.lidalia.exampleapp.system.logging.{LogbackLoggingDefinition, LoggerFactory, StaticLoggerFactory}
import uk.org.lidalia.scalalang.{PoolFactory, ResourceFactory}
import uk.org.lidalia.webdriver.{ReusableWebDriver, WebDriverDefinition}

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
  loggerFactory: LoggerFactory[Logger] = StaticLoggerFactory,
  environmentFactory: ResourceFactory[Environment] = EnvironmentDefinition(),
  webDriverFactory: ResourceFactory[ReusableWebDriver] = WebDriverDefinition()
)
