package uk.org.lidalia
package exampleapp
package tests.website

import uk.org.lidalia.exampleapp.tests.library.scalatest.AroundSuites
import uk.org.lidalia.exampleapp.tests.support.{FunctionalTestEnvironment, FunctionalTestEnvironmentFactory}

class WebsiteTestSuites(
  factoryData: Either[FunctionalTestEnvironmentFactory, FunctionalTestEnvironment]
) extends AroundSuites(
  factoryData
) {

  override def suites(factories: FunctionalTestEnvironment) = List(

    new LoginTests(factories.environmentFactory, factories.webDriverFactory),
    new RegisterTests(factories.environmentFactory, factories.webDriverFactory)

  )

  def this(testEnv: FunctionalTestEnvironment) = this(Right(testEnv))
  def this() = this(Left(FunctionalTestEnvironmentFactory()))
}
