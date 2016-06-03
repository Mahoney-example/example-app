package uk.org.lidalia
package exampleapp.tests

import library.scalatest.AroundSuites
import uk.org.lidalia.exampleapp.tests.api.ApiTestSuites
import uk.org.lidalia.exampleapp.tests.support.{FunctionalTestEnvironment, FunctionalTestEnvironmentFactory}
import uk.org.lidalia.exampleapp.tests.website.WebsiteTestSuites

class FunctionalTestSuites extends AroundSuites(Left(FunctionalTestEnvironmentFactory())) {

  override def suites(factories: FunctionalTestEnvironment) = List(
    new ApiTestSuites(factories),
    new WebsiteTestSuites(factories)
  )
}
