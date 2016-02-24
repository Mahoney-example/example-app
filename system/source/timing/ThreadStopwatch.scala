package uk.org.lidalia
package exampleapp.system.timing

import java.time.{Instant, Clock}

import collection.mutable
import util.Try

object ThreadStopwatch {

  private val threadLocalResult = new InheritableThreadLocal[?[MutableStopwatchResult[_, _]]] {
    override def initialValue() = None
  }

  def apply(clock: Clock = Clock.systemDefaultZone()) = new ThreadStopwatch(clock)

  def time[I, O](input: I)(work: => O): O = {
    if (ThreadStopwatch.threadLocalResult.get().isDefined) {
      new ThreadStopwatch().time(input)(work).output.get
    } else {
      work
    }
  }
}

class ThreadStopwatch(clock: Clock = Clock.systemDefaultZone()) {

  def time[I, O](input: I)(work: => O): StopwatchResult[I, O] = {

    val parentResultOption = ThreadStopwatch.threadLocalResult.get()
    val clockToUse = parentResultOption.map(_.clock).getOrElse(clock)
    val mutableResult = new MutableStopwatchResult[I, O](clockToUse, input)
    ThreadStopwatch.threadLocalResult.set(Some(mutableResult))

    val result = Try(work)

    val stopwatchResult = mutableResult.complete(result).toImmutable
    parentResultOption.map { _.subResults.append(stopwatchResult) }

    ThreadStopwatch.threadLocalResult.set(parentResultOption)
    stopwatchResult
  }
}

private class MutableStopwatchResult[I, O](
  val clock: Clock,
  val input: I
) {

  val start = clock.instant()

  private var _output: ?[Try[O]] = None

  def output = _output.get

  private var _end: Instant = null

  def end = _end

  def complete(output: Try[O]) = {
    _end = clock.instant()
    _output = output
    this
  }

  val subResults: mutable.Buffer[StopwatchResult[_, _]] = mutable.Buffer()

  def toImmutable: StopwatchResult[I, O] = {
    new StopwatchResult(start, end, input, output, subResults.toList)
  }
}
