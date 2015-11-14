package uk.org.lidalia
package exampleapp
package system

import java.sql.DriverManager

import org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric
import org.hsqldb.jdbc.{JDBCDataSource, JDBCDriver}
import uk.org.lidalia.net.Uri

object HsqlDatabaseDefinition {

  def apply(
    name: String = randomAlphanumeric(5)
  ) = {
    new HsqlDatabaseDefinition(
        JdbcConfig(
          Uri(s"jdbc:hsqldb:mem:$name"),
          "sa",
          "",
          "SELECT 1"
      )
    )
  }
}

class HsqlDatabaseDefinition private (
  override val jdbcConfig: JdbcConfig
) extends DatabaseDefinition {

  override def using[T](work: (Database) => T): T = {
    DriverManager.registerDriver(new JDBCDriver())
    val dataSource = new JDBCDataSource
  }

}
