package uk.org.lidalia
package exampleapp
package system

import java.sql.DriverManager

import org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric
import org.hsqldb.persist.HsqlProperties
import org.hsqldb.{DatabaseManager, DatabaseURL}
import org.hsqldb.jdbc.{JDBCDataSource, JDBCDriver}
import uk.org.lidalia.net.Uri

object MemDatabaseDefinition {

  def apply(
    name: String = randomAlphanumeric(5)
  ) = {
    new MemDatabaseDefinition(name)
  }
}

class MemDatabaseDefinition private (
  name: String
) extends DatabaseDefinition {

  override val jdbcConfig = JdbcConfig(
    Uri(s"jdbc:hsqldb:mem:$name"),
    "sa",
    "",
    "SELECT 1"
  )
  override def using[T](work: (Database) => T): T = {
    val db = DatabaseManager.getDatabase(DatabaseURL.S_MEM, name, new HsqlProperties)

    DriverManager.registerDriver(new JDBCDriver())

    val dataSource = new JDBCDataSource
    dataSource.setUrl(jdbcConfig.jdbcUrl.toString)
    dataSource.setUser(jdbcConfig.username)
    dataSource.setPassword(jdbcConfig.password)

    val database = new Database(dataSource)
    try {
      work(database)
    } finally {
      db.close(0)
    }
  }
}
