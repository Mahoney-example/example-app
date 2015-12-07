package uk.org.lidalia
package exampleapp
package system.db

import java.sql.Connection
import javax.sql.DataSource

import liquibase.changelog.DatabaseChangeLog
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import uk.org.lidalia.exampleapp.system.HasLogger
import uk.org.lidalia.exampleapp.system.db.changelog.Migrator
import uk.org.lidalia.scalalang.{ResourceFactory, Reusable}
import ResourceFactory._try

object Database {

  def apply(config: JdbcConfig): Database = apply(config, DriverManagerDataSource(config))

  def apply(config: JdbcConfig, dataSource: DataSource): Database = new Database(config, dataSource)

}

class Database private (
  val jdbcConfig: JdbcConfig,
  val dataSource: DataSource
) extends Reusable with ResourceFactory[Connection] with HasLogger {

  override def using[T](work: (Connection) => T): T = {

    val connection = dataSource.getConnection

    _try {
      work(connection)
    } _finally {
      connection.close()
    }
  }

  def update(changelog: DatabaseChangeLog): Unit = {
    using { connection =>
      log.info("DB Update start")
      val liquibaseDb = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection))
      Migrator(changelog, liquibaseDb).update()
      log.info("DB Update end")
    }
  }
}
