package uk.org.lidalia.exampleapp.system

object RemoteDatabaseDefinition {
  def apply(jdbcConfig: JdbcConfig) = new RemoteDatabaseDefinition(jdbcConfig)
}

class RemoteDatabaseDefinition private (
  val jdbcConfig: JdbcConfig
) extends DatabaseDefinition {

  override def using[T](work: (Database) => T): T = ???
}
