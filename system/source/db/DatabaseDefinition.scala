package uk.org.lidalia
package exampleapp.system.db

import uk.org.lidalia.scalalang.ResourceFactory

trait DatabaseDefinition extends ResourceFactory[Database] {

  val jdbcConfig: JdbcConfig

}
