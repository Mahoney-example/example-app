package uk.org.lidalia
package exampleapp.tests

import org.scalatest.{Args, Status, Suite, Suites}
import uk.org.lidalia.exampleapp.local.Environment
import uk.org.lidalia.exampleapp.tests.library.ReusableWebDriver
import uk.org.lidalia.exampleapp.tests.support.FunctionalTestEnvironment
import uk.org.lidalia.scalalang.ResourceFactory

class FunctionalTestSuites extends Suite {

  private def suites(
    envFactory: ResourceFactory[Environment],
    webDriverFactory: ResourceFactory[ReusableWebDriver]
  ) = List(

    new LoginTests(envFactory, webDriverFactory),
    new RegisterTests(envFactory, webDriverFactory)
  )

  override def run(testName: Option[String], args: Args): Status = {

    FunctionalTestEnvironment().using { factories =>

      new Suites(
        suites(factories._1, factories._2): _*
      ).run(testName, args)
    }
  }
}
