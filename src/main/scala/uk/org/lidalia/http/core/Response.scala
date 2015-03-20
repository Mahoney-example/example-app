package uk.org.lidalia.http.core

import uk.org.lidalia.net2.Uri
import org.joda.time.DateTime

object Response {
  def apply[T](
    status: Code,
    headerFields: List[HeaderField] = Nil,
    body: T = None): Response[T] = {

    new Response(ResponseHeader(status, headerFields), body)
  }

  def apply[T](
    responseHeader: ResponseHeader,
    body: T): Response[T] = {

    new Response(responseHeader, body)
  }
}

class Response[T] private(val responseHeader: ResponseHeader, entity: T) extends Message(responseHeader, entity) {

  val code = responseHeader.code

  def requiresRedirect: Boolean = responseHeader.requiresRedirect

  def location: ?[Uri] = responseHeader.location

  def date: ?[DateTime] = responseHeader.date

  def isNotError: Boolean = responseHeader.isNotError
  def isInformational: Boolean = responseHeader.isInformational
  def isSuccessful: Boolean = responseHeader.isSuccessful
  def isRedirection: Boolean = responseHeader.isRedirection
  def isClientError: Boolean = responseHeader.isClientError
  def isServerError: Boolean = responseHeader.isServerError
  def isError: Boolean = responseHeader.isError
}
