package uk.org.lidalia
package exampleapp
package system.db


import org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric
import org.hsqldb.persist.HsqlProperties
import org.hsqldb.{DatabaseManager, DatabaseURL}
import org.hsqldb.jdbc.JDBCDriver
import uk.org.lidalia.exampleapp.system.HasLogger
import uk.org.lidalia.net.Uri

object MemDatabaseDefinition {

  JDBCDriver.driverInstance

  def apply(
    name: String = randomAlphanumeric(5)
  ) = {
    new MemDatabaseDefinition(name)
  }
}

class MemDatabaseDefinition private (
  name: String
) extends DatabaseDefinition with HasLogger {

  override val jdbcConfig = JdbcConfig(
    Uri(s"jdbc:hsqldb:mem:$name"),
    "sa",
    "",
    "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS"
  )

  override def using[T](work: (Database) => T): T = {

    val db = DatabaseManager.getDatabase(DatabaseURL.S_MEM, name, new HsqlProperties)
    log.info(s"Created database $name")

    val database = Database(jdbcConfig)


    try {

      database.using { connection =>
        connection.createStatement().execute(
          """
            |SET DATABASE TRANSACTION CONTROL MVCC;
            |SET FILES LOB SCALE 1;
          """.stripMargin
        )
      }

      work(database)
    } finally {
      db.close(0)
      log.info(s"Deleted database $name")
    }
  }
}
