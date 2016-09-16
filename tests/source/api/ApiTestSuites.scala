package uk.org.lidalia
package exampleapp
package tests.api

import uk.org.lidalia.exampleapp.tests.library.scalatest.AroundSuites
import uk.org.lidalia.exampleapp.tests.support.{FunctionalTestEnvironment, FunctionalTestEnvironmentFactory}

class ApiTestSuites(
  factoryData: Either[FunctionalTestEnvironmentFactory, FunctionalTestEnvironment]
) extends AroundSuites(
  factoryData
) {

  override def suites(testEnv: FunctionalTestEnvironment) = List(

    new CreateUserTests(testEnv.environmentFactory)

  )

  def this(testEnv: FunctionalTestEnvironment) = this(Right(testEnv))
  def this() = this(Left(FunctionalTestEnvironmentFactory()))
}
