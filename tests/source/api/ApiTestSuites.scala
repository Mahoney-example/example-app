package uk.org.lidalia
package exampleapp
package tests.api

import org.scalatest.Suite
import uk.org.lidalia.exampleapp.tests.library.scalatest.AroundSuites
import uk.org.lidalia.exampleapp.tests.support.{FunctionalTestEnvironment, FunctionalTestEnvironmentFactory}

import scala.collection.immutable.Seq

class ApiTestSuites(
  factoryData: Either[FunctionalTestEnvironmentFactory, FunctionalTestEnvironment]
) extends AroundSuites(
  factoryData
) {
  override def suites(testEnv: FunctionalTestEnvironment): Seq[Suite] = List(
    new CreateUserTests(testEnv.environmentFactory)
  )

  def this(testEnv: FunctionalTestEnvironment) = this(Right(testEnv))
  def this() = this(Left(FunctionalTestEnvironmentFactory()))
}
