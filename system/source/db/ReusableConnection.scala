package uk.org.lidalia.exampleapp.system.db

import java.sql.Connection

import uk.org.lidalia.scalalang.Reusable
import uk.org.lidalia.scalalang.Reusable.{BROKEN, OK, State}

class ReusableConnection(conn: Connection) extends DelegatingConnection(conn) with Reusable {

  override def check: State = {
    try {
      if (isValid(1)) OK else BROKEN
    } catch { case e: Exception =>
      Reusable.BROKEN
    }
  }

  override def reset(): Unit = {
    rollback()
  }
}
