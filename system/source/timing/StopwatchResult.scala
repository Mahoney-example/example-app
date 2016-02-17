package uk.org.lidalia.exampleapp.system.timing

import java.time.Duration.between
import java.time.Instant
import java.time.Instant.now

case class StopwatchResult[+I, +O](
  start: Instant,
  end: Instant,
  input: I,
  output: O,
  subResults: List[StopwatchResult[_, _]] = Nil
) {
  def elapsed = between(start, end)

  def periods: List[StopwatchResult[_, _]] = {
    val allButOne = subResults.foldRight(List[StopwatchResult[_, _]]()) { (stopwatchResult, accumulated) =>
      stopwatchResult :: selfTimeIfNecessary(accumulated, stopwatchResult.end)
    }
    selfTimeIfNecessary(allButOne, start)
  }

  def selfTimeIfNecessary(acc: List[StopwatchResult[_, _]], selfStart: Instant): List[StopwatchResult[_, _]] = {
    def selfEnd = acc.headOption.map(_.start).getOrElse(end)
    if (selfEnd.isAfter(selfStart)) {
      StopwatchResult(selfStart, selfEnd, "Self time", None) :: acc
    } else {
      acc
    }
  }
}
