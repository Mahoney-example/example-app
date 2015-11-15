package uk.org.lidalia
package exampleapp
package system.db

import uk.org.lidalia.net.Uri

case class JdbcConfig(
  jdbcUrl: Uri,
  username: String,
  password: String,
  checkQuery: String
)
