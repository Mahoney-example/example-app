package uk.org.lidalia
package exampleapp.server.adapters.outbound.email

import exampleapp.server.domain.{Email, EmailResult, EmailService, Success}
import scalalang.Lock

import scala.collection.{immutable, mutable}
import scala.concurrent.Future

object MemEmailService {

  def apply() = new MemEmailService()

}

class MemEmailService private () extends EmailService {

  private val lock = new Lock()
  private val emails: mutable.Buffer[Email] = mutable.Buffer()

  override def send(email: Email): Future[EmailResult] = {
    lock.writeLock.using {
      emails.append(email)
    }
    Future.successful(Success)
  }

  def emailsSent(): immutable.Seq[Email] = {
    lock.readLock.using {
      emails.toList
    }
  }
}
