package uk.org.lidalia
package exampleapp.tests.support

import org.scalatest.fixture

trait EnvironmentTests
extends fixture.FunSuite
with FixtureSuite[TestEnvironment]
with TestEnvironmentProvider
