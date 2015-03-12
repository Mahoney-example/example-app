package uk.org.lidalia.http.client

import java.io.InputStream

import uk.org.lidalia.http.core.{ResponseHeader, MediaRangePref}

abstract class Accept[T](mediaRangePrefs: List[MediaRangePref]) extends uk.org.lidalia.http.core.headerfields.Accept(mediaRangePrefs) {
  def handle(request: TargetedRequest[T], response: ResponseHeader, entityBytes: InputStream): T
}
