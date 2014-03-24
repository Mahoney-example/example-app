package uk.org.lidalia.http.core

import uk.org.lidalia.net2.{Uri, PathAndQuery}
import uk.org.lidalia.lang.RichObject

object RequestUri {
  def apply(requestUri: String): RequestUri = apply(PathAndQuery(requestUri))
  def apply(requestUri: Either[Uri, PathAndQuery]): RequestUri = new RequestUri(requestUri)
}

class RequestUri private(@Identity requestUri: Either[Uri, PathAndQuery]) extends RichObject {

  override lazy val toString = requestUri.fold(left => left.toString, right => right.toString)
}
