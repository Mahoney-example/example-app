package uk.org.lidalia
package exampleapp.tests

import uk.org.lidalia.exampleapp.tests.library.AroundSuites
import uk.org.lidalia.exampleapp.tests.support.{FunctionalTestEnvironmentFactory, FunctionalTestEnvironment}

class FunctionalTestSuites extends AroundSuites(FunctionalTestEnvironmentFactory()) {

  override def suites(factories: FunctionalTestEnvironment) = List(

    new LoginTests(factories.environmentFactory, factories.webDriverFactory),
    new RegisterTests(factories.environmentFactory, factories.webDriverFactory)
  )
}
