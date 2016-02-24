package uk.org.lidalia
package exampleapp.system.timing

import java.time.Clock

import ThreadStopwatch.timeWithResults
import ThreadStopwatch.time
import scalalang.ResourceFactory
import uk.org.lidalia.exampleapp.system.timing.TimedResourceFactory.{noOpTimingNotifier, TimingNotifier}

object TimedResourceFactory {
  type TimingNotifier = (StopwatchResult[_,_]) => Unit
  val noOpTimingNotifier: TimingNotifier = (res) => {}

  def timed[R](
    decorated: ResourceFactory[R],
    timingNotifier: TimingNotifier = noOpTimingNotifier,
    clock: Clock = Clock.systemDefaultZone(),
    name: ?[String] = None
  ): ResourceFactory[R] = {
    new TimedResourceFactory[R](
      decorated,
      timingNotifier,
      clock,
      name
    )
  }
}

private class TimedResourceFactory[+R] private (
  decorated: ResourceFactory[R],
  timingNotifier: TimingNotifier = noOpTimingNotifier,
  clock: Clock = Clock.systemDefaultZone(),
  name: ?[String] = None
) extends ResourceFactory[R] {

  override def using[T](work: (R) => T): T = {

    val timingName = "TimedResource."+name.getOrElse(decorated.toString)

    val result = timeWithResults(timingName+".Total", clock) {
      decorated.using { resource =>
        time(timingName+"."+resource+".Usage", clock) {
          work(resource)
        }
      }
    }

    timingNotifier(result)

    result.output.get
  }
}
