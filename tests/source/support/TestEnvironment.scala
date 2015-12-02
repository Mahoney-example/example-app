package uk.org.lidalia.exampleapp.tests.support

import org.openqa.selenium.WebDriver
import uk.org.lidalia.exampleapp.local.Environment

case class TestEnvironment(
  environment: Environment,
  webDriver: ReusableWebDriver
)
