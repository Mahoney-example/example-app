package api
import org.scalatest.Outcome
import uk.org.lidalia.exampleapp.local.{Environment, EnvironmentDefinition}
import uk.org.lidalia.scalalang.ResourceFactory

class CreateUserTests(envDef: ResourceFactory[Environment]) extends org.scalatest.fixture.FunSuite {

  test("Can create a user") {env =>

  }

  override type FixtureParam = Environment

  override protected def withFixture(test: OneArgTest): Outcome = {
    envDef.using { env =>
      test.apply(env)
    }
  }

  def this() = this(EnvironmentDefinition())
}
