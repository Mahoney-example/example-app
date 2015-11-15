package uk.org.lidalia
package exampleapp
package system.db

import java.sql.Connection
import javax.sql.DataSource

import uk.org.lidalia.scalalang.{Reusable, ResourceFactory}

object Database {

  def apply(config: JdbcConfig): Database = apply(DriverManagerDataSource(config))

  def apply(dataSource: DataSource): Database = new Database(dataSource)

}

class Database private (
  val dataSource: DataSource
) extends Reusable with ResourceFactory[Connection] {

  override def using[T](work: (Connection) => T): T = {

    val connection = dataSource.getConnection

    try {
      work(connection)
    } finally {
      connection.close()
    }
  }
}
