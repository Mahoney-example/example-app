package uk.org.lidalia
package exampleapp.server.domain

import scala.concurrent.Future

trait EmailService {

  def send(email: Email): Future[EmailResult]

}
