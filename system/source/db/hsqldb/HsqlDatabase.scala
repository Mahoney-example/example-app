package uk.org.lidalia.exampleapp.system.db.hsqldb

import javax.sql.DataSource

import liquibase.changelog.DatabaseChangeLog
import uk.org.lidalia.exampleapp.system.HasLogger
import uk.org.lidalia.exampleapp.system.db.changelog.UpdatableDatabase
import uk.org.lidalia.exampleapp.system.db.{DriverManagerDataSource, JdbcConfig}
import uk.org.lidalia.scalalang.Reusable

object HsqlDatabase {

  def apply(
    config: JdbcConfig,
    changelog: DatabaseChangeLog
  ): HsqlDatabase = {
    apply(
      config,
      DriverManagerDataSource(config),
      changelog
    )
  }

  def apply(
    config: JdbcConfig,
    dataSource: DataSource,
    changelog: DatabaseChangeLog
  ): HsqlDatabase = new HsqlDatabase(config, dataSource, changelog)

}

class HsqlDatabase protected (
  val jdbcConfig: JdbcConfig,
  dataSource: DataSource,
  changelog: DatabaseChangeLog
) extends UpdatableDatabase(
  dataSource,
  changelog
) with Reusable with HasLogger {

  override def reset(): Unit = {
    using { connection =>
      connection.createStatement().execute(
        "DROP SCHEMA PUBLIC CASCADE"
      )
    }
    update()
  }
}
