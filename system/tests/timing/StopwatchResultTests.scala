package uk.org.lidalia.exampleapp.system.timing

import java.time.{Duration, Instant}
import java.time.Instant.now

import org.scalatest.FunSuite

class StopwatchResultTests extends FunSuite {

  test("periods returns self time if no subResults") {

    val start = now()
    val result = StopwatchResult(
      start,
      start.plusSeconds(5),
      "input A",
      "output A"
    )

    assert(result.elapsed == Duration.ofSeconds(5))
    assert(result.periods == List(StopwatchResult(start, start.plusSeconds(5), "Self time", None)))
  }

  test("calculates periods") {

    val start = now()
    val result = StopwatchResult(
      start,
      start.plusSeconds(5),
      "input A",
      "output A",
      List(
        StopwatchResult(
          start.plusSeconds(1),
          start.plusSeconds(2),
          "input AA",
          "output AA"
        ),
        StopwatchResult(
          start.plusSeconds(3),
          start.plusSeconds(4),
          "input AB",
          "output AB"
        )
      )
    )

    assert(result.elapsed == Duration.ofSeconds(5))
    assert(result.periods == List(
      StopwatchResult(
        start,
        start.plusSeconds(1),
        "Self time",
        None
      ),
      StopwatchResult(
        start.plusSeconds(1),
        start.plusSeconds(2),
        "input AA",
        "output AA"
      ),
      StopwatchResult(
        start.plusSeconds(2),
        start.plusSeconds(3),
        "Self time",
        None
      ),
      StopwatchResult(
        start.plusSeconds(3),
        start.plusSeconds(4),
        "input AB",
        "output AB"
      ),
      StopwatchResult(
        start.plusSeconds(4),
        start.plusSeconds(5),
        "Self time",
        None
      )
    ))
  }

  test("handles periods with no gaps") {

    val start = now()
    val result = StopwatchResult(
      start,
      start.plusSeconds(2),
      "input A",
      "output A",
      List(
        StopwatchResult(
          start,
          start.plusSeconds(1),
          "input AA",
          "output AA"
        ),
        StopwatchResult(
          start.plusSeconds(1),
          start.plusSeconds(2),
          "input AB",
          "output AB"
        )
      )
    )

    assert(result.elapsed == Duration.ofSeconds(2))
    assert(result.periods == List(
      StopwatchResult(
        start,
        start.plusSeconds(1),
        "input AA",
        "output AA"
      ),
      StopwatchResult(
        start.plusSeconds(1),
        start.plusSeconds(2),
        "input AB",
        "output AB"
      )
    ))
  }
}
