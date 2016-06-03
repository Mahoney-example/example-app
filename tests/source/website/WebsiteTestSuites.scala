package uk.org.lidalia
package exampleapp
package tests.website

import org.scalatest.Suite
import uk.org.lidalia.exampleapp.tests.library.scalatest.AroundSuites
import uk.org.lidalia.exampleapp.tests.support.{FunctionalTestEnvironment, FunctionalTestEnvironmentFactory}

import scala.collection.immutable.Seq

class WebsiteTestSuites(factoryData: Either[FunctionalTestEnvironmentFactory, FunctionalTestEnvironment]) extends AroundSuites(factoryData) {

  override def suites(factories: FunctionalTestEnvironment): Seq[Suite] = List(

    new LoginTests(factories.environmentFactory, factories.webDriverFactory),
    new RegisterTests(factories.environmentFactory, factories.webDriverFactory)

  )

  def this(testEnvFactory: FunctionalTestEnvironmentFactory) = this(Left(testEnvFactory))
  def this(testEnv: FunctionalTestEnvironment) = this(Right(testEnv))
  def this() = this(FunctionalTestEnvironmentFactory())
}
