package uk.org.lidalia.exampleapp.server.application

import uk.org.lidalia.exampleapp.system.db.JdbcConfig
import uk.org.lidalia.net.Url

case class ApplicationConfig (
  jdbcConfig: JdbcConfig,
  sendGridUrl: Url,
  sendGridToken: String,
  contentfulUrl: Url
)
