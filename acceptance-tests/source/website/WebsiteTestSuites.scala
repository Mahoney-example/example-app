package uk.org.lidalia.exampleapp.acceptancetests.website

import uk.org.lidalia.exampleapp.acceptancetests.support.{FunctionalTestEnvironment, FunctionalTestEnvironmentFactory}
import uk.org.lidalia.scalatest.AroundSuites
import uk.org.lidalia.scalatest.AroundSuites.FactoryData

class WebsiteTestSuites(factoryData: FactoryData[FunctionalTestEnvironment])
  extends AroundSuites(factoryData) {

  override def suites(factories: FunctionalTestEnvironment) = List(

    new LoginTests(factories.environmentFactory, factories.webDriverFactory),
    new RegisterTests(factories.environmentFactory, factories.webDriverFactory)

  )

  def this(testEnv: FunctionalTestEnvironment) = this(Right(testEnv))
  def this() = this(Left(FunctionalTestEnvironmentFactory()))
}
