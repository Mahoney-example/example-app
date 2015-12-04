package uk.org.lidalia.exampleapp.tests.functional

import uk.org.lidalia.exampleapp.tests.support.{TestEnvironmentDefinition, TestEnvironment, EnvironmentTests}
import uk.org.lidalia.scalalang.ResourceFactory

class RegisterTests(envDefinition: ResourceFactory[TestEnvironment]) extends EnvironmentTests(envDefinition) {

  def this() = this(TestEnvironmentDefinition())

  test("hello world") { environment =>
    println("hello world")
  }
}
