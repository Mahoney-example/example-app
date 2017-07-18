package uk.org.lidalia.exampleapp.system.db

import java.io.PrintWriter
import java.sql.Connection
import java.util.logging.Logger
import javax.sql.DataSource

import uk.org.lidalia.scalalang.{ManuallyClosedResource, ResourceFactory}

class ConnectionFactoryDataSource(resourceFactory: ResourceFactory[Connection]) extends DataSource {

  override def getConnection: Connection = new ManuallyClosedConnection(ManuallyClosedResource(resourceFactory))

  override def getConnection(username: String, password: String): Connection = ???

  override def unwrap[T](iface: Class[T]): T = this.asInstanceOf[T]

  override def isWrapperFor(iface: Class[_]): Boolean = iface.isAssignableFrom(getClass)

  override def setLogWriter(out: PrintWriter): Unit = ???

  override def getLoginTimeout: Int = ???

  override def setLoginTimeout(seconds: Int): Unit = ???

  override def getParentLogger: Logger = ???

  override def getLogWriter: PrintWriter = ???
}

class ManuallyClosedConnection(manuallyClosedConnection: ManuallyClosedResource[Connection]) extends DelegatingConnection(manuallyClosedConnection.get()) {
  override def close(): Unit = manuallyClosedConnection.close()
}
