package uk.org.lidalia
package exampleapp
package system.db

import java.sql.{Array, Blob, CallableStatement, Clob, Connection, DatabaseMetaData, NClob, PreparedStatement, SQLWarning, SQLXML, Savepoint, Statement, Struct}
import java.util
import java.util.Properties
import java.util.concurrent.Executor
import javax.sql.DataSource

import uk.org.lidalia.scalalang.Reusable.{State, OK, BROKEN}
import uk.org.lidalia.scalalang.{ResourceFactory, Reusable}
import uk.org.lidalia.scalalang.TryFinally._try

object Database {

  def apply(
    config: JdbcConfig
  ): Database = {
    apply(
      DriverManagerDataSource(config)
    )
  }

  def apply(
    dataSource: DataSource
  ): Database = new Database(dataSource)

}

class Database protected (
  dataSource: DataSource
) extends ResourceFactory[ReusableConnection] {

  override def using[T](work: (ReusableConnection) => T): T = {

    val connection = new ReusableConnection(dataSource.getConnection)

    _try {
      work(connection)
    } _finally {
      connection.close()
    }
  }
}


