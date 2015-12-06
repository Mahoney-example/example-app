package uk.org.lidalia
package exampleapp.tests

import org.scalatest.Suites
import uk.org.lidalia.exampleapp.tests.support.TestEnvironmentProvider

class FunctionalTests extends Suites(
   new LoginTests,
   new RegisterTests
) with TestEnvironmentProvider
