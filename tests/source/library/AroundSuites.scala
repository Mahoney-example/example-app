package uk.org.lidalia.exampleapp.tests.library

import org.scalatest.{Args, Status, Suite, Suites}
import uk.org.lidalia.scalalang.ResourceFactory

abstract class AroundSuites[R](resourceFactory: ResourceFactory[R]) extends Suite {

  def suites(resource: R): List[Suite]

  override def run(testName: Option[String], args: Args): Status = {

    resourceFactory.using { resource =>

      val status = new Suites(
        suites(resource): _*
      ).run(testName, args)

      status.waitUntilCompleted()

      status
    }
  }
}
