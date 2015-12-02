package uk.org.lidalia.exampleapp.server.services.email

import uk.org.lidalia.scalalang.Lock

import collection.mutable
import collection.immutable

import concurrent.Future

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
