package uk.org.lidalia
package exampleapp.tests

import org.slf4j.LoggerFactory
import uk.org.lidalia.exampleapp.tests.support.BrowserFunctionalTests

class RegisterTests extends BrowserFunctionalTests {

  test("register") { environment =>
    LoggerFactory.getLogger(classOf[RegisterTests]).info("Test in progress")
  }

  test("register2") { environment =>
    LoggerFactory.getLogger(classOf[RegisterTests]).info("Test in progress")
  }
}
