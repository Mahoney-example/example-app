package uk.org.lidalia.exampleapp.server.web

import uk.org.lidalia.exampleapp.server.application.ApplicationConfig
import uk.org.lidalia.net.Port

case class ServerConfig(
  applicationConfig: ApplicationConfig,
  localPort: Option[Port]
)
