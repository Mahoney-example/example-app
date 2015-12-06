package uk.org.lidalia
package exampleapp.tests

import org.slf4j.LoggerFactory
import uk.org.lidalia.exampleapp.tests.support.EnvironmentTests

class LoginTests extends EnvironmentTests {

  test("login") { environment =>
    LoggerFactory.getLogger(classOf[LoginTests]).info("Test in progress")
  }
}
