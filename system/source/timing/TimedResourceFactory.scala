package uk.org.lidalia
package exampleapp.system.timing

import java.time.Clock

import ThreadStopwatch.timeWithResults
import ThreadStopwatch.time
import scalalang.NoOpValues.noop
import scalalang.ResourceFactory
import uk.org.lidalia.exampleapp.system.timing.TimedResourceFactory.TimingConsumer

object TimedResourceFactory {

  type TimingConsumer = (StopwatchResult[_,_]) => Unit

  def timed[R](
    decorated: ResourceFactory[R],
    timingConsumer: TimingConsumer = noop,
    clock: Clock = Clock.systemDefaultZone(),
    name: ?[String] = None
  ): ResourceFactory[R] = {
    new TimedResourceFactory[R](
      decorated,
      timingConsumer,
      clock,
      name
    )
  }
}

private class TimedResourceFactory[+R] private (
  decorated: ResourceFactory[R],
  timingConsumer: TimingConsumer = noop,
  clock: Clock = Clock.systemDefaultZone(),
  name: ?[String] = None
) extends ResourceFactory[R] {

  override def using[T](work: (R) => T): T = {

    val timingName = "TimedResource."+name.getOrElse(decorated.toString)

    val result = timeWithResults(timingName+".Total", clock) {
      decorated.using { resource =>
        time(timingName+"."+resource+".Usage") {
          work(resource)
        }
      }
    }

    timingConsumer(result)

    result.output.get
  }
}
