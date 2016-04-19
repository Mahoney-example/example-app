package uk.org.lidalia
package exampleapp.tests.support

import ch.qos.logback.classic.Level.INFO
import org.scalatest.ConfigMap
import org.slf4j.LoggerFactory
import uk.org.lidalia.exampleapp.local.{Environment, EnvironmentDefinition}
import uk.org.lidalia.exampleapp.system.display.{Display, DisplayFactory}
import uk.org.lidalia.scalalang.{PoolFactory, ResourceFactory}

trait TestEnvironmentProvider
extends FixtureProvider[TestEnvironment]
with LoggingConfiguredSuite
{
  override protected def metaFactory(config: ConfigMap) = {
    new ResourceFactory[ResourceFactory[TestEnvironment]] {
      override def using[T](work: (ResourceFactory[TestEnvironment]) => T): T = {
        LoggerFactory.getLogger(classOf[TestEnvironmentProvider]).info("Using the Metafactory")
        DisplayFactory().using { display =>
          PoolFactory(
            TestEnvironmentDefinition(
              WebDriverDefinition(display)
            )
          ).using(work)
        }
      }
    }

  }

  override protected val logLevels = List(
    "uk.org.lidalia" -> INFO
  )
}


class Something extends ResourceFactory[(ResourceFactory[Environment], ResourceFactory[ReusableWebDriver])] {
  override def using[T](work: ((ResourceFactory[Environment], ResourceFactory[ReusableWebDriver])) => T): T = ???
}
