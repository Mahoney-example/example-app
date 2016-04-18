package uk.org.lidalia
package exampleapp.tests.support

import org.scalatest.{ConfigMap, Args, Status, Suite}
import uk.org.lidalia.exampleapp.system.display.DisplayFactory
import uk.org.lidalia.scalalang.ResourceFactory

trait FixtureProvider[R] extends Suite {

  protected def metaFactory(config: ConfigMap): ResourceFactory[ResourceFactory[R]]

  protected lazy val configKey = classOf[FixtureProvider[_]].getName

  override def run(testName: Option[String], args: Args): Status = {

    if (args.configMap.contains(configKey)) {
      super.run(testName, args)
    } else {
      metaFactory(args.configMap).using { factory =>
        super.run(testName, args.copy(configMap = args.configMap.+(configKey -> factory)))
      }
    }
  }
}

trait FixtureSuite[R] extends FixtureProvider[R] with org.scalatest.fixture.Suite {

  override type FixtureParam = R

  override protected def withFixture(test: OneArgTest) = {

    val envDefinition = test.configMap.getRequired[ResourceFactory[R]](configKey)

    envDefinition.using { environment =>
      test.apply(environment)
    }
  }

}
