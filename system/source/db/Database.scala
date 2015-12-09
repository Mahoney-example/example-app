package uk.org.lidalia
package exampleapp
package system.db

import java.sql.Connection
import javax.sql.DataSource

import liquibase.changelog.DatabaseChangeLog
import uk.org.lidalia.scalalang.ResourceFactory
import ResourceFactory._try

object Database {

  def apply(
    config: JdbcConfig,
    changelog: DatabaseChangeLog
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
) extends ResourceFactory[Connection] {

  override def using[T](work: (Connection) => T): T = {

    val connection = dataSource.getConnection

    _try {
      work(connection)
    } _finally {
      connection.close()
    }
  }
}
