package uk.org.lidalia.exampleapp.tests.support

import uk.org.lidalia.exampleapp.local.Environment
import uk.org.lidalia.scalalang.Reusable

class TestEnvironment(
  val environment: Environment,
  val webDriver: ReusableWebDriver
) extends Reusable {

  override def reset() = {
    environment.reset()
    webDriver.reset()
  }
}
