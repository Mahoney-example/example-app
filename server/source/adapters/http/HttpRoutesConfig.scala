package uk.org.lidalia
package exampleapp.server.adapters.http

import exampleapp.server.domain.DomainConfig
import net.Port

case class HttpRoutesConfig(
  domainConfig: DomainConfig,
  localPort: Option[Port]
)
