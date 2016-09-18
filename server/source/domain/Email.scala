package uk.org.lidalia
package exampleapp.server.domain

case class Email(
  to: Set[EmailAddress],
  cc: Set[EmailAddress],
  bcc: Set[EmailAddress],
  from: EmailAddress,
  subject: String,
  body: String
)

case class EmailAddress(value: String)

sealed class EmailResult
case object Success extends EmailResult
case object Failure extends EmailResult
