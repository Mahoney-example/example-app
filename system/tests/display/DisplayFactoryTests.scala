package uk.org.lidalia
package exampleapp.system.display

import org.scalatest.FunSuite
import scalalang.ResourceFactory
import scalalang.os.Windows
import scalalang.os.Linux

import util.Random

class DisplayFactoryTests extends FunSuite {

  test("returns none if operating system does not support it") {
    val expectedOutcome = Random.nextInt()

    val outcome = DisplayFactory(osFamily = Windows).using { optionalDisplay =>
      (optionalDisplay, expectedOutcome)
    }

    assert(outcome == (None, expectedOutcome))
  }

  test("returns none if underlying process not installed") {
    val expectedOutcome = Random.nextInt()

    val outcome = DisplayFactory(osFamily = Linux, command = "not_a_real_command").using { optionalDisplay =>
      (optionalDisplay, expectedOutcome)
    }

    assert(outcome == (None, expectedOutcome))
  }

  test("creates a display") {

    val expectedOutcome = Random.nextInt()

    val outcome = DisplayFactory(osFamily = Linux).using { optionalDisplay =>
      (optionalDisplay, expectedOutcome)
    }

    assert(outcome._1.isDefined)
    assert(outcome._2 == expectedOutcome)
  }

}
