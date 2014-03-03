package uk.org.lidalia.http.core

import uk.org.lidalia.lang.RichObject

abstract class Message private[http](@Identity val header: MessageHeader) extends RichObject {

  def headerFieldValues(headerFieldName: String) = header.headerFieldValues(headerFieldName)

  def headerField(headerFieldName: String) = header.headerField(headerFieldName)

  def headerField[T](headerFieldName: HeaderFieldName[T]) = header.headerField(headerFieldName)

  val headerFields = header.headerFields

  val headerFieldMap = header.headerFieldMap

}