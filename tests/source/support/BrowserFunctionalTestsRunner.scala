package uk.org.lidalia
package exampleapp
package tests
package support

import org.scalatest.Suite
import uk.org.lidalia.exampleapp.local.Environment
import uk.org.lidalia.exampleapp.tests.library.ReusableWebDriver
import uk.org.lidalia.scalalang.ResourceFactory

class BrowserFunctionalTestsRunner(
  suiteBuilder: (ResourceFactory[Environment], ResourceFactory[ReusableWebDriver]) => Suite
) extends App {

  FunctionalTestEnvironment().using { factories =>
    suiteBuilder(
      factories._1,
      factories._2
    ).execute(durations = true, fullstacks = true, stats = true)
  }
}
