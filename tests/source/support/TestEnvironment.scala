package uk.org.lidalia.exampleapp.tests.support

import uk.org.lidalia.exampleapp.local.Environment
import uk.org.lidalia.scalalang.Reusable

case class TestEnvironment(
  environment: Environment,
  webDriver: ReusableWebDriver
) extends Reusable
