package uk.org.lidalia
package exampleapp.server.adapters.outbound

import net.Url
import uk.org.lidalia.exampleapp.system.db.JdbcConfig

case class OutboundAdaptersConfig(
  jdbcConfig: JdbcConfig,
  sendGridUrl: Url,
  sendGridToken: String
)
