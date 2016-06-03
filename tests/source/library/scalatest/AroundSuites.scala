package uk.org.lidalia
package exampleapp
package tests
package library.scalatest

import org.scalatest.{Args, Status, Suite, Suites}
import uk.org.lidalia.scalalang.ResourceFactory

import scala.collection.immutable

abstract class AroundSuites[R](resourceData: Either[ResourceFactory[R], R]) extends Suite {

  def suites(resource: R): immutable.Seq[Suite]

  override def run(testName: Option[String], args: Args): Status = {

    resourceData.fold(
      { resourceFactory =>
        runUsingResource(testName, args, resourceFactory)
      },
      { resource =>
        runSuites(testName, args, resource)
      }
    )
  }

  def runUsingResource(testName: Option[String], args: Args, resourceFactory: ResourceFactory[R]): Status = {
    resourceFactory.using { resource =>

      val status = runSuites(testName, args, resource)

      status.waitUntilCompleted()

      status
    }
  }

  def runSuites(testName: Option[String], args: Args, resource: R): Status = {
    new Suites(
      suites(resource): _*
    ).run(testName, args)
  }
}
