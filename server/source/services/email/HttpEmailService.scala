package uk.org.lidalia.exampleapp.server.services.email

import uk.org.lidalia.net.Url

import concurrent.Future

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
