package uk.org.lidalia
package exampleapp.tests.functional

import org.slf4j.LoggerFactory
import uk.org.lidalia.exampleapp.tests.support.{EnvironmentTests, TestEnvironment, TestEnvironmentDefinition}
import uk.org.lidalia.scalalang.ResourceFactory

class LoginTests(
  envDefinition: ResourceFactory[TestEnvironment]
) extends EnvironmentTests(
  envDefinition
) {

  def this() = this(TestEnvironmentDefinition())

  test("login") { environment =>
    LoggerFactory.getLogger(classOf[LoginTests]).info("Test in progress")
  }
}
