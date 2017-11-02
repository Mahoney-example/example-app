package uk.org.lidalia.exampleapp.acceptancetests

import uk.org.lidalia.scalatest.AroundSuites
import uk.org.lidalia.scalatest.AroundSuites.FactoryData
import uk.org.lidalia.exampleapp.acceptancetests.api.ApiTestSuites
import uk.org.lidalia.exampleapp.acceptancetests.support.{FunctionalTestEnvironment, FunctionalTestEnvironmentFactory}
import uk.org.lidalia.exampleapp.acceptancetests.website.WebsiteTestSuites

class AcceptanceTestSuites(
  factoryData: FactoryData[FunctionalTestEnvironment]
) extends AroundSuites(factoryData) {

  override def suites(factories: FunctionalTestEnvironment) = List(

    new ApiTestSuites(factories),
    new WebsiteTestSuites(factories)

  )

  def this(testEnv: FunctionalTestEnvironment) = this(Right(testEnv))
  def this() = this(Left(FunctionalTestEnvironmentFactory()))
}
