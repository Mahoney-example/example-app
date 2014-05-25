package uk.org.lidalia.http.core

import uk.org.lidalia.lang.RichObject

abstract class MessageHeader private[http](val headerFields: List[HeaderField]) extends RichObject {

  @Identity val headerFieldMap: Map[String, HeaderField] = headerFields.groupBy(_.name).map({
    case (name, headerFieldsForName) => (name, HeaderField(name, headerFieldsForName.map(_.values).flatten))
  })

  def headerField[T](headerFieldName: HeaderFieldName[T]): T = {
    val values = headerFieldValues(headerFieldName.name)
    headerFieldName.parse(values)
  }

  def headerFieldValues(headerFieldName: String): List[String] = headerField(headerFieldName).?(_.values) or List()

  def headerField(headerFieldName: String): ?[HeaderField] = headerFieldMap.get(headerFieldName)

  override def toString = headerFields.map(_.toString).mkString("\r\n")
}
