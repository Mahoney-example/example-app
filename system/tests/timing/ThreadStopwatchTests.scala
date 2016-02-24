package uk.org.lidalia.exampleapp.system.timing

import java.time.Duration.ofSeconds
import java.time.Instant

import org.scalatest.FunSuite
import uk.org.lidalia.exampleapp.system.timing.ThreadStopwatch.timeWithResults

import util.{Success, Failure}

class ThreadStopwatchTests extends FunSuite {

  test("measures time spent") {

    val start = Instant.now()
    val clock = new MutableFixedClock(start)

    val result = timeWithResults("some work", clock) {
      clock.fastForward(ofSeconds(1))
      "result"
    }

    assert(result == StopwatchResult(
      start = start,
      end = start.plusSeconds(1),
      input = "some work",
      output = Success("result")
    ))
  }

  test("propagates exception") {

    val start = Instant.now()
    val clock = new MutableFixedClock(start)

    val result = timeWithResults("some work", clock) {
      clock.fastForward(ofSeconds(1))
      "result"
    }

    assert(result == StopwatchResult(
      start = start,
      end = start.plusSeconds(1),
      input = "some work",
      output = Success("result")
    ))
  }

  test("measures sub periods") {

    val start = Instant.now()
    val clock = new MutableFixedClock(start)

    val result = timeWithResults("top work", clock) {
      clock.fastForward(ofSeconds(1))
      ThreadStopwatch.time("child work 1") {
        clock.fastForward(ofSeconds(1))
        "child result 1"
      }
      clock.fastForward(ofSeconds(1))
      ThreadStopwatch.time("child work 2") {
        clock.fastForward(ofSeconds(1))
        "child result 2"
      }
      clock.fastForward(ofSeconds(1))
      "top result"
    }

    assert(result == StopwatchResult(
      start = start,
      end = start.plusSeconds(5),
      input = "top work",
      output = Success("top result"),
      List(
        StopwatchResult(
          start = start.plusSeconds(1),
          end = start.plusSeconds(2),
          input = "child work 1",
          output = Success("child result 1"),
          Nil
        ),
        StopwatchResult(
          start = start.plusSeconds(3),
          end = start.plusSeconds(4),
          input = "child work 2",
          output = Success("child result 2"),
          Nil
        )
      )
    ))
  }

  test("handles exceptions") {


    val start = Instant.now()
    val clock = new MutableFixedClock(start)
    val thrown = new Exception("Oh no")

    val result = timeWithResults("top work", clock) {
      clock.fastForward(ofSeconds(1))
      ThreadStopwatch.time("child work 1") {
        clock.fastForward(ofSeconds(1))
        "child result 1"
      }
      clock.fastForward(ofSeconds(1))
      ThreadStopwatch.time("child work 2") {
        clock.fastForward(ofSeconds(1))
        throw thrown
      }
      clock.fastForward(ofSeconds(1))
      "top result"
    }

    assert(result == StopwatchResult(
      start = start,
      end = start.plusSeconds(4),
      input = "top work",
      output = Failure(thrown),
      List(
        StopwatchResult(
          start = start.plusSeconds(1),
          end = start.plusSeconds(2),
          input = "child work 1",
          output = Success("child result 1")
        ),
        StopwatchResult(
          start = start.plusSeconds(3),
          end = start.plusSeconds(4),
          input = "child work 2",
          output = Failure(thrown)
        )
      )
    ))
  }

  test("nested returning time calls") {

    val start = Instant.now()
    val clock = new MutableFixedClock(start)

    val result = timeWithResults("top work", clock) {
      clock.fastForward(ofSeconds(1))
      timeWithResults("child work 1") {
        clock.fastForward(ofSeconds(1))
        "child result 1"
      }
      clock.fastForward(ofSeconds(1))
      "top result"
    }

    assert(result == StopwatchResult(
      start = start,
      end = start.plusSeconds(3),
      input = "top work",
      output = Success("top result"),
      List(
        StopwatchResult(
          start = start.plusSeconds(1),
          end = start.plusSeconds(2),
          input = "child work 1",
          output = Success("child result 1"),
          Nil
        )
      )
    ))
  }
}


