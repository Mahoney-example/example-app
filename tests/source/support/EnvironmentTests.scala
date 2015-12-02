package uk.org.lidalia.exampleapp.tests.support

import org.scalatest.{Outcome, fixture}
import org.slf4j.LoggerFactory
import uk.org.lidalia.scalalang.ResourceFactory

object EnvironmentTests {
  val logger = LoggerFactory.getLogger(classOf[EnvironmentTests])
}
abstract class EnvironmentTests(envDefinition: ResourceFactory[TestEnvironment]) extends fixture.FunSuite {

  override type FixtureParam = TestEnvironment

  override protected def withFixture(test: OneArgTest): Outcome = {
    EnvironmentTests.logger.info("********* STARTING TEST")
    val result = envDefinition.using { environment =>
      test.apply(environment)
    }
    EnvironmentTests.logger.info("********* TEST DONE")
    result
  }
}
