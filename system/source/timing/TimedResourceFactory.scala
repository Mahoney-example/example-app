package uk.org.lidalia.exampleapp.system.timing

import java.time.{Instant, Clock}

import uk.org.lidalia.scalalang.ResourceFactory

class TimedResourceFactory[+R](
  decorated: ResourceFactory[R],
  clock: Clock = Clock.systemDefaultZone()
) extends ResourceFactory[R] {

  override def using[T](work: (R) => T): T = {

    val start = clock.instant()
    var gotResource: Instant = null
    var doneWork: Instant = null
    decorated.using { resource =>
      var gotResource = clock.instant()

      val result = work(resource)
      var doneWork = clock.instant()
      result
    }
//    val end = clock.instant()
  }
}

trait TimingNotifier {

}
