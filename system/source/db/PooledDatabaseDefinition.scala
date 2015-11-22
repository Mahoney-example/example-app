package uk.org.lidalia
package exampleapp
package system.db

import com.zaxxer.hikari.HikariDataSource
import uk.org.lidalia.scalalang.ResourceFactory._try

object PooledDatabaseDefinition {

  def apply(jdbcConfig: JdbcConfig) = new PooledDatabaseDefinition(jdbcConfig)

}

class PooledDatabaseDefinition private (
  jdbcConfig: JdbcConfig
) extends DatabaseDefinition {

  override def using[T](work: (Database) => T): T = {

    val ds = new HikariDataSource()
    ds.setJdbcUrl(jdbcConfig.jdbcUrl.toString)
    ds.setUsername(jdbcConfig.username)
    ds.setPassword(jdbcConfig.password)
    ds.setConnectionTestQuery(jdbcConfig.checkQuery)

    val db = Database(jdbcConfig, ds)

    _try {
      work(db)
    } _finally {
      ds.close()
    }
  }
}
