package uk.org.lidalia.exampleapp.tests.support

import uk.org.lidalia.exampleapp.local.{Environment, EnvironmentDefinition}
import uk.org.lidalia.scalalang.ResourceFactory
import uk.org.lidalia.scalalang.ResourceFactory.usingAll

object TestEnvironmentDefinition {

  def apply(
    webDriverDefinition: ResourceFactory[ReusableWebDriver] = WebDriverDefinition(),
    environmentDefinition: ResourceFactory[Environment] = EnvironmentDefinition()) = {
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
