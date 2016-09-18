package uk.org.lidalia
package exampleapp.server.domain

import exampleapp.system.db.JdbcConfig
import net.Url

case class DomainConfig(
  jdbcConfig: JdbcConfig,
  sendGridUrl: Url,
  sendGridToken: String
)
