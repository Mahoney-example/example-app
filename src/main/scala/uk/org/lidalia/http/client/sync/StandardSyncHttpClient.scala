package uk.org.lidalia.http.client.sync

import java.time.Duration

import uk.org.lidalia.http.client.{Apache4Client, ExpectedEntityHttpClient, SyncHttpClient}
import uk.org.lidalia.http.core.Method.GET
import uk.org.lidalia.http.core._
import uk.org.lidalia.lang.UnsignedByte
import uk.org.lidalia.net2.Url

import scala.collection.immutable.Seq

object StandardSyncHttpClient {

  def get(
    url: Url
  ): Response[Seq[UnsignedByte]] = {
    execute(GET, url)
  }

//  def get[T](
//    url: Url,
//    accept: Accept[T],
//    headerFields: HeaderField*
//  ): Response[T] = ???

//  def execute[T](
//                  method: Method,
//                  url: Url,
//                  accept: Accept[T],
//                  headerFields: HeaderField*): Result[T] = {
//
//    decorated.execute(
//      requestFor(method, url, headerFields.toList, accept, List(
//        Host := url.hostAndPort,
//        accept
//      ))
//    )
//  }
  def execute(method: Method, url: Url): Response[Seq[UnsignedByte]] = {
    getClient(url).execute(Request(method, RequestUri(Right(url.pathAndQuery)), Nil))
  }

  private def getClient(url: Url) = {
    new SyncHttpClient(
      new ExpectedEntityHttpClient(
        new Apache4Client(url.baseUrl)
      ),
      Duration.ofSeconds(5)
    )
  }
}
