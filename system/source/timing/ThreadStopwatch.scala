package uk.org.lidalia
package exampleapp.system.timing

import java.time.{Instant, Clock}

import collection.mutable
import util.Try

object ThreadStopwatch {

  private val threadLocalResult: ThreadLocal[MutableStopwatchResult[_, _]] = new InheritableThreadLocal[MutableStopwatchResult[_, _]]

  def apply(clock: Clock = Clock.systemDefaultZone()) = new ThreadStopwatch(clock)

  def time[I, O](input: I)(work: => O): O = {
    Option(threadLocalResult.get()).map { parentResult =>

      val mutableResult = new MutableStopwatchResult[I, O](parentResult.clock, input)
      threadLocalResult.set(mutableResult)

      val result = Try(work)
      mutableResult.output = result
      mutableResult.end = parentResult.clock.instant()
      parentResult.subResults.append(mutableResult.toImmutable)
      threadLocalResult.set(parentResult)

      result.get

    }.getOrElse(work)
  }
}

class ThreadStopwatch(clock: Clock = Clock.systemDefaultZone()) {

  def time[I, O](input: I)(work: => O): StopwatchResult[I, O] = {

    val mutableResult = new MutableStopwatchResult[I, O](clock, input)
    ThreadStopwatch.threadLocalResult.set(mutableResult)

    try {
      val result = Try(work)
      mutableResult.output = result
      mutableResult.end = clock.instant()
      mutableResult.toImmutable
    } finally {
      ThreadStopwatch.threadLocalResult.remove()
    }
  }
}

private class MutableStopwatchResult[I, O](
  val clock: Clock,
  val input: I
) {

  val start = clock.instant()

  var output: ?[Try[O]] = None

  var end: Instant = null

  val subResults: mutable.Buffer[StopwatchResult[_, _]] = mutable.Buffer()

  def toImmutable: StopwatchResult[I, O] = {
    new StopwatchResult(start, end, input, output.get, subResults.toList)
  }
}
