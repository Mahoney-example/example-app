package uk.org.lidalia
package exampleapp
package system.db

import com.zaxxer.hikari.HikariDataSource

object PooledDatabaseDefinition {

  def apply(jdbcConfig: JdbcConfig) = new PooledDatabaseDefinition(jdbcConfig)

}

class PooledDatabaseDefinition private (
  override val jdbcConfig: JdbcConfig
) extends DatabaseDefinition {

  override def using[T](work: (Database) => T): T = {

    val ds = new HikariDataSource()
    ds.setJdbcUrl(jdbcConfig.jdbcUrl.toString)
    ds.setUsername(jdbcConfig.username)

    val db = Database(ds)

    try {
      work(db)
    } finally {
      ds.close()
    }
  }
}
