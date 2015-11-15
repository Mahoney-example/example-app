package uk.org.lidalia
package exampleapp
package system.db

import java.io.PrintWriter
import java.sql.{SQLFeatureNotSupportedException, SQLException, DriverManager}
import javax.sql.DataSource

object DriverManagerDataSource {
  def apply(jdbcConfig: JdbcConfig) = new DriverManagerDataSource(jdbcConfig)
}

class DriverManagerDataSource private(
  jdbcConfig: JdbcConfig
) extends DataSource {

  override def getConnection = {
    getConnection(
      jdbcConfig.username,
      jdbcConfig.password
    )
  }

  override def getConnection(
    username: String,
    password: String
  ) = {
    DriverManager.getConnection(jdbcConfig.jdbcUrl.toString, username, password)
  }

  override def setLogWriter(out: PrintWriter) = DriverManager.setLogWriter(out)

  override def getLoginTimeout = DriverManager.getLoginTimeout

  override def setLoginTimeout(seconds: Int) = DriverManager.setLoginTimeout(seconds)

  override def getParentLogger = throw new SQLFeatureNotSupportedException(s"$getClass does not use java.util.Logging")

  override def getLogWriter = DriverManager.getLogWriter

  override def unwrap[T](iface: Class[T]) = {
    if (isWrapperFor(iface))
      this.asInstanceOf[T]
    else
      throw new SQLException(s"Cannot unwrap $this to $iface")
  }

  override def isWrapperFor(iface: Class[_]) = {
    iface == classOf[DriverManagerDataSource]
  }
}
