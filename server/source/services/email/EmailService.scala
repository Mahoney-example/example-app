package uk.org.lidalia.exampleapp.server.services.email

import concurrent.Future

trait EmailService {

  def send(email: Email): Future[EmailResult]

}
