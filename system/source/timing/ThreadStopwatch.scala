package uk.org.lidalia
package exampleapp.system.timing

import java.time.{Instant, Clock}

import collection.mutable
import util.Try

object ThreadStopwatch {

  private val threadLocalResult = new InheritableThreadLocal[?[MutableStopwatchResult[_, _]]] {
    override def initialValue() = None
  }

  def time[I, O](input: I)(work: => O): O = {
    if (ThreadStopwatch.threadLocalResult.get().isDefined) {
      timeWithResults(input)(work).output.get
    } else {
      work
    }
  }

  def timeWithResults[I, O](input: I, clock: Clock = Clock.systemDefaultZone())(work: => O): StopwatchResult[I, O] = {

    val parentResultOption = ThreadStopwatch.threadLocalResult.get()
    val clockToUse = parentResultOption.map(_.clock).getOrElse(clock)
    val mutableResult = new MutableStopwatchResult[I, O](clockToUse, input)
    ThreadStopwatch.threadLocalResult.set(Some(mutableResult))

    val result = Try(work)

    val stopwatchResult = mutableResult.complete(result).toImmutable
    parentResultOption.foreach { _.subResults.append(stopwatchResult) }

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
