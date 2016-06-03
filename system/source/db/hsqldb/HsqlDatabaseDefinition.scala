package uk.org.lidalia
package exampleapp.system.db.hsqldb

import liquibase.changelog.DatabaseChangeLog
import org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric
import org.hsqldb.persist.HsqlProperties
import org.hsqldb.{DatabaseManager, DatabaseURL}
import uk.org.lidalia.exampleapp.system.HasLogger
import uk.org.lidalia.exampleapp.system.db.JdbcConfig
import uk.org.lidalia.net.Uri
import uk.org.lidalia.scalalang.ResourceFactory
import uk.org.lidalia.scalalang.TryFinally._try

object HsqlDatabaseDefinition {

  def apply(
    changelog: DatabaseChangeLog,
    name: ?[String] = None
  ) = {
    new HsqlDatabaseDefinition(changelog, name)
  }
}

class HsqlDatabaseDefinition private (
  changelog: DatabaseChangeLog,
  name: ?[String]
) extends ResourceFactory[HsqlDatabase] with HasLogger {

  private def jdbcConfig(dbName: String) = JdbcConfig(
    Uri(s"jdbc:hsqldb:mem:$dbName"),
    "sa",
    "",
    "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS"
  )

  override def using[T](work: (HsqlDatabase) => T): T = {

    val dbName = name.getOrElse(randomAlphanumeric(5))

    val db = DatabaseManager.getDatabase(DatabaseURL.S_MEM, dbName, new HsqlProperties)
    log.info(s"Created database $dbName")

    val database = HsqlDatabase(jdbcConfig(dbName), changelog)

    _try {

      database.using { connection =>
        connection.createStatement().execute(
          """
            |SET DATABASE TRANSACTION CONTROL MVCC;
            |SET FILES LOB SCALE 1;
          """.stripMargin.trim
        )
      }

      work(database)
    } _finally {
      db.close(0)
      log.info(s"Deleted database $dbName")
    }
  }
}
