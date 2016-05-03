package uk.org.lidalia
package exampleapp.tests

import org.scalatest.Suites
import uk.org.lidalia.exampleapp.tests.library.{LogbackLoggerFactoryPerRun, WebDriverDefinitionPerRun}
import uk.org.lidalia.exampleapp.tests.support.EnvironmentDefinitionPerRun

class FunctionalTestSuites extends Suites(
   new LoginTests,
   new RegisterTests
) with EnvironmentDefinitionPerRun
  with WebDriverDefinitionPerRun
  with LogbackLoggerFactoryPerRun
