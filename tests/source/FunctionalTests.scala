package uk.org.lidalia
package exampleapp.tests

import scalalang.{PoolFactory, ResourceFactory}
import support.{TestEnvironment, TestEnvironmentDefinition, TestSuite}
import uk.org.lidalia.exampleapp.tests.functional.RegisterTests

class FunctionalTests(
  envDefinition: ?[ResourceFactory[TestEnvironment]]
) extends TestSuite(envDefinition) {

  def this() = this(None)

  override def nestedTests(factory: ResourceFactory[TestEnvironment]) = List(
    new RegisterTests(factory),
    new RegisterTests(factory)
  )

  override def metaFactory = PoolFactory(TestEnvironmentDefinition())
}
