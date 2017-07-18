package uk.org.lidalia.exampleapp.system.db

import java.sql.{Connection, SQLException}

import org.mockito.Mockito.when
import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar

class ReusableConnectionTest extends FunSuite with MockitoSugar {

  test("isWrapperFor returns true for ReusableConnection") {

    val rootConn = mock[Connection]
    val conn: Connection = new ReusableConnection(new WrappedConnection(rootConn))
    when(rootConn.isWrapperFor(classOf[OtherWrappedConnection])).thenReturn(false)

    assert(
      conn.isWrapperFor(classOf[ReusableConnection]) &&
      conn.isWrapperFor(classOf[Connection]) &&
      conn.isWrapperFor(classOf[WrappedConnection]) &&
      !conn.isWrapperFor(classOf[OtherWrappedConnection]) &&
      !conn.isWrapperFor(classOf[String])
    )
  }

  test("unwrap returns correct type for ReusableConnection") {

    val rootConn = mock[Connection]
    when(rootConn.unwrap(classOf[OtherWrappedConnection])).thenThrow(new SQLException())
    val conn: Connection = new ReusableConnection(new WrappedConnection(rootConn))


    val unwrapped1: ReusableConnection = conn.unwrap(classOf[ReusableConnection])
    val unwrapped2: Connection = conn.unwrap(classOf[Connection])
    val unwrapped3: WrappedConnection = conn.unwrap(classOf[WrappedConnection])

    intercept[SQLException] {
      conn.unwrap(classOf[OtherWrappedConnection])
    }
  }

  class WrappedConnection(conn: Connection) extends DelegatingConnection(conn)

  class OtherWrappedConnection(conn: Connection) extends DelegatingConnection(conn)
}
