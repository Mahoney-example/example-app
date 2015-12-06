package uk.org.lidalia
package exampleapp.tests.support

import org.scalatest.{Args, Status, Suite}
import uk.org.lidalia.scalalang.ResourceFactory

trait FixtureProvider[R] extends Suite {

  type FixtureParam = R

  protected val metaFactory: ResourceFactory[ResourceFactory[R]]

  protected lazy val configKey = metaFactory.getClass.getName

  override def run(testName: Option[String], args: Args): Status = {

    if (args.configMap.contains(configKey)) {
      super.run(testName, args)
    } else {
      metaFactory.using { factory =>
        super.run(testName, args.copy(configMap = args.configMap.+(configKey -> factory)))
      }
    }
  }
}

trait FixtureSuite[R] extends FixtureProvider[R] with org.scalatest.fixture.Suite {

  override protected def withFixture(test: OneArgTest) = {

    val envDefinition = test.configMap.getRequired[ResourceFactory[R]](configKey)

    envDefinition.using { environment =>
      test.apply(environment)
    }
  }

}
