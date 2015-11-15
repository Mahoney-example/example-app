package uk.org.lidalia
package exampleapp
package system.db

import java.sql.Connection
import javax.sql.DataSource

import liquibase.changelog.DatabaseChangeLog
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import uk.org.lidalia.exampleapp.system.db.changelog.Migrator
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

  def update(changelog: DatabaseChangeLog): Unit = {
    using { connection =>
      val liquibaseDb = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection))
      Migrator(changelog, liquibaseDb).update()
    }
  }
}
