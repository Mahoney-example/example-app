package uk.org.lidalia
package exampleapp.server.adapters.outbound.email

import exampleapp.server.domain.{Email, EmailResult, EmailService}
import net.Url

import scala.concurrent.Future

object HttpEmailService {

  def apply(
    url: Url,
    token: String
  ) = {
    new HttpEmailService(url, token)
  }
}

class HttpEmailService private(
  url: Url,
  token: String
) extends EmailService {
  override def send(email: Email): Future[EmailResult] = ???
}
