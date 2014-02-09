package uk.org.lidalia.http.core

import org.scalatest
import scalatest.PropSpec
import scalatest.prop.TableDrivenPropertyChecks
import scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import uk.org.lidalia.net2.EqualsChecks.{possibleArgsFor, reflexiveTest, equalsTest}

@RunWith(classOf[JUnitRunner])
class HeaderFieldTest extends PropSpec with TableDrivenPropertyChecks {

  property("HeaderField equals contract") {
    val argsForHeaderField = possibleArgsFor(
      List("Name1", "Name2"),
      List(List("value1"), List("value1", "value2"), List("value2"))
    )

    reflexiveTest(argsForHeaderField) { (args) =>
      HeaderField(args._1, args._2)
    }

    equalsTest(argsForHeaderField) { (args) =>
      HeaderField(args._1, args._2)
    }
  }
}
