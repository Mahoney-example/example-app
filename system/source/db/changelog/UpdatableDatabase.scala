package uk.org.lidalia
package exampleapp.system
package db.changelog

import javax.sql.DataSource

import liquibase.changelog.DatabaseChangeLog
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import uk.org.lidalia.exampleapp.system.db.Database
import uk.org.lidalia.scalalang.Reusable

class UpdatableDatabase protected (
  dataSource: DataSource,
  changelog: DatabaseChangeLog
) extends Database(
  dataSource
) with Reusable with HasLogger {

  def update(): Unit = {
    using { connection =>
      log.info("DB Update start")
      val liquibaseDb = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection))
      Migrator(changelog, liquibaseDb).update()
      log.info("DB Update end")
    }
  }
}
