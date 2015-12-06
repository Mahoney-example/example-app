package uk.org.lidalia
package exampleapp.tests.support

import org.scalatest.{Args, ConfigMap, Status, Suite}
import uk.org.lidalia.scalalang.ResourceFactory

trait FixtureProvider[R] extends Suite {

  type FixtureParam = R

  override def run(testName: Option[String], args: Args): Status = {

    if (args.configMap.contains("fixtureFactory")) {
      super.run(testName, args)
    } else {
      metaFactory.using { factory =>
        super.run(testName, args.copy(configMap = args.configMap.+("fixtureFactory" -> factory)))
      }
    }
  }

  def metaFactory: ResourceFactory[ResourceFactory[R]]
}
