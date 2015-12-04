package uk.org.lidalia.exampleapp.tests.functional

import org.slf4j.LoggerFactory
import uk.org.lidalia.exampleapp.tests.support.{TestEnvironmentDefinition, TestEnvironment, EnvironmentTests}
import uk.org.lidalia.scalalang.ResourceFactory

class RegisterTests(envDefinition: ResourceFactory[TestEnvironment]) extends EnvironmentTests(envDefinition) {

  def this() = this(TestEnvironmentDefinition())

  test("hello world") { environment =>
    LoggerFactory.getLogger(classOf[RegisterTests]).info("Test in progress")
  }
}
