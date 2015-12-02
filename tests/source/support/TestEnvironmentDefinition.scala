package uk.org.lidalia.exampleapp.tests.support

import org.openqa.selenium.WebDriver
import uk.org.lidalia.exampleapp.local.{Environment, EnvironmentDefinition}
import uk.org.lidalia.scalalang.ResourceFactory
import uk.org.lidalia.scalalang.ResourceFactory.usingAll

object TestEnvironmentDefinition {

  def apply(
    webDriverDefinition: WebDriverDefinition = WebDriverDefinition(),
    environmentDefinition: EnvironmentDefinition = EnvironmentDefinition()) = {
    new TestEnvironmentDefinition(
      webDriverDefinition,
      environmentDefinition
    )
  }
}

class TestEnvironmentDefinition private(
  webDriverDefinition: ResourceFactory[ReusableWebDriver],
  environmentDefinition: ResourceFactory[Environment]
) extends ResourceFactory[TestEnvironment] {

  override def using[T](work: (TestEnvironment) => T): T = {
    usingAll(webDriverDefinition, environmentDefinition) { (webDriver, env) =>
      work(new TestEnvironment(env, webDriver))
    }
  }
}
