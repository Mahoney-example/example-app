package uk.org.lidalia
package exampleapp.system

import java.io.PrintWriter
import java.sql.Connection
import java.util.logging.Logger
import javax.sql.DataSource

import net.Uri
import scalalang.{Reusable, ResourceFactory}

trait DatabaseDefinition extends ResourceFactory[Database] {

  val jdbcConfig: JdbcConfig

}

class Database private[system] (
  dataSource: DataSource
) extends DataSource with Reusable {

  override def getConnection = dataSource.getConnection

  override def getConnection(username: String, password: String) = dataSource.getConnection(username, password)

  override def unwrap[T](iface: Class[T]) = dataSource.unwrap(iface)

  override def isWrapperFor(iface: Class[_]) = dataSource.isWrapperFor(iface)

  override def getLogWriter = dataSource.getLogWriter

  override def setLogWriter(out: PrintWriter) = dataSource.setLogWriter(out)

  override def getLoginTimeout = dataSource.getLoginTimeout

  override def setLoginTimeout(seconds: Int) = dataSource.setLoginTimeout(seconds)

  override def getParentLogger = dataSource.getParentLogger
}

case class JdbcConfig(
  jdbcUrl: Uri,
  username: String,
  password: String,
  checkQuery: String
)
