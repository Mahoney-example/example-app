package uk.org.lidalia.http.client

import uk.org.lidalia.http.core.Method._
import uk.org.lidalia.http.core.headerfields.Host
import uk.org.lidalia.http.core.{Request, RequestUri}
import uk.org.lidalia.net2.Url

object ConvenientHttpClient {
  def apply[Result[_]](
    decorated: BaseHttpClient[Result] = ExpectedEntityHttpClient()) = {
    new ConvenientHttpClient(decorated)
  }
}

class ConvenientHttpClient[Result[_]](decorated: BaseHttpClient[Result]) extends BaseHttpClient[Result] {

  def get[T](
    url: Url,
    accept: Accept[T]): Result[T] =

    decorated.execute(
      new DirectedRequest(
        url.scheme,
        url.hostAndPort,
        Request(
          GET,
          RequestUri(url.pathAndQuery),
          List(
            Host(url.hostAndPort),
            accept
          )
        ),
        accept
      )
    )

  override def execute[T](request: DirectedRequest[T]) = decorated.execute(request)
}
