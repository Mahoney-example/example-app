package uk.org.lidalia.exampleapp.acceptancetests.api

import uk.org.lidalia.exampleapp.acceptancetests.support.{FunctionalTestEnvironment, FunctionalTestEnvironmentFactory}
import uk.org.lidalia.scalatest.AroundSuites
import uk.org.lidalia.scalatest.AroundSuites.FactoryData

class ApiTestSuites(
  factoryData: FactoryData[FunctionalTestEnvironment]
) extends AroundSuites(factoryData) {

  override def suites(testEnv: FunctionalTestEnvironment) = List(

    new CreateUserTests(testEnv.environmentFactory)

  )

  def this(testEnv: FunctionalTestEnvironment) = this(Right(testEnv))
  def this() = this(Left(FunctionalTestEnvironmentFactory()))
}
