package uk.org.lidalia.exampleapp.system.timing

import java.time.temporal.TemporalAmount
import java.time.{Clock, ZoneId, Instant}

class MutableFixedClock(
  var time: Instant = Instant.now(),
  override val getZone: ZoneId = Clock.systemDefaultZone().getZone
) extends Clock {

  def fastForward(duration: TemporalAmount): MutableFixedClock = {
    time = time.plus(duration)
    this
  }

  def rewind(duration: TemporalAmount): MutableFixedClock = {
    time = time.minus(duration)
    this
  }

  override def instant(): Instant = time

  override def withZone(zone: ZoneId): Clock = new MutableFixedClock(time, zone)
}
