package uk.org.lidalia
package exampleapp.system.timing

import java.time.Clock

import util.Success

object Stopwatch {

  def time[I, O](input: I, clock: Clock = Clock.systemDefaultZone())(work: => O): StopwatchResult[I, O] = {

    val start = clock.instant()
    val result = work
    val end = clock.instant()

    val stopwatchResult = StopwatchResult(
      start = start,
      end = end,
      input = input,
      output = Success(result)
    )

    stopwatchResult
  }

  def timeWithChildResults[I, O](input: I, clock: Clock = Clock.systemDefaultZone())(work: => (O, List[StopwatchResult[_, _]])): StopwatchResult[I, O] = {

    val start = clock.instant()
    val result = work
    val end = clock.instant()

    val stopwatchResult = StopwatchResult(
      start = start,
      end = end,
      input = input,
      output = Success(result._1),
      subResults = result._2
    )

    stopwatchResult
  }

}
