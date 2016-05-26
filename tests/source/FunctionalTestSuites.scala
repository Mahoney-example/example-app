package uk.org.lidalia
package exampleapp.tests

import org.scalatest.{ParallelTestExecution, Suites}
import uk.org.lidalia.exampleapp.local.Environment
import uk.org.lidalia.exampleapp.tests.library.ReusableWebDriver
import uk.org.lidalia.exampleapp.tests.support.BrowserFunctionalTestsRunner
import uk.org.lidalia.scalalang.ResourceFactory

class FunctionalTestSuites(
  envFactory: ResourceFactory[Environment],
  webDriverFactory: ResourceFactory[ReusableWebDriver]
) extends Suites(
   new LoginTests(envFactory, webDriverFactory),
   new RegisterTests(envFactory, webDriverFactory)
) with ParallelTestExecution

object FunctionalTestSuites extends BrowserFunctionalTestsRunner(new FunctionalTestSuites(_, _))
