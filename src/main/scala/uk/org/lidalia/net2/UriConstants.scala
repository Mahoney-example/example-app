package uk.org.lidalia.net2

import java.util.regex.Pattern

object UriConstants {

  val unreserved =
    ('a' to 'z').toSet ++
    ('A' to 'Z') ++
    ('0' to '9') ++
    Set('-', '.', '_', '~')

  val subDelims = Set(
    '!',
    '$',
    '&',
    '\'',
    '(',
    ')',
    '*',
    '+',
    ',',
    ';',
    '=')

  val pchar =
    unreserved ++
    subDelims ++
    Set(':', '@')

  private val unreservedRange =
      "a-z" +
      "A-Z" +
      "0-9" +
      "-" +
      "." +
      "_" +
      "~"

  private val subDelimsRange =
      "!" +
      "$" +
      "&" +
      "'" +
      "(" +
      ")" +
      "*" +
      "+" +
      "," +
      ";" +
      "="

  val queryParamKeyChars =
        unreservedRange +
        subDelimsRange +
        "@" +
        "/" +
        "?"
  val queryParamValueChars = queryParamKeyChars + "="
  val queryChars = queryParamValueChars + "&"

  val hexDigitRegex = "[0-9A-Fa-f]"
  val pctEncodedRegex = s"%$hexDigitRegex{2}"
  val unreservedRegex = s"[$unreservedRange]"
  val subDelimsRegex = s"[$subDelimsRange]"
  val pcharRegex = s"$unreservedRegex|$pctEncodedRegex|$subDelimsRegex|[:@]"

  val queryRegex = s"($pcharRegex|/|\\?)*"
  val queryParamValueRegex = queryRegex.replace("&", "")
  val queryParamKeyRegex = queryParamValueRegex.replace("=", "")

  object Patterns {
    val query = Pattern.compile(queryRegex)
    val queryParamValue = Pattern.compile(queryParamValueRegex)
    val queryParamKey = Pattern.compile(queryParamKeyRegex)
    val fragment = query
  }

  def requireRegex(clazz: Class[_], value: String, pattern: Pattern) = {
    require(pattern.matcher(value).matches(),
      s"${clazz.getSimpleName} $value must match ${pattern.pattern}")
  }
}
