package uk.org.lidalia
package exampleapp.tests

import org.slf4j.LoggerFactory
import uk.org.lidalia.exampleapp.tests.support.BrowserFunctionalTests

class LoginTests extends BrowserFunctionalTests {

  Range(1, 15).foreach { index =>
    test(s"login $index") { environment =>
      LoggerFactory.getLogger(classOf[LoginTests]).info(s"Test $index in progress")
    }
  }
}
