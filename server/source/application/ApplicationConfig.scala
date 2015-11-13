package uk.org.lidalia.exampleapp.server.application

import uk.org.lidalia.exampleapp.system.JdbcConfig
import uk.org.lidalia.net.Url

case class ApplicationConfig (
  jdbcConfig: JdbcConfig,
  sendGridUrl: Url,
  sendGridToken: String,
  contentfulUrl: Url
)
