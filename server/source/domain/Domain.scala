package uk.org.lidalia
package exampleapp.server.domain

import org.slf4j.Logger
import exampleapp.server.adapters.outbound.Adapters
import exampleapp.system.logging.LoggerFactory
import scalalang.Reusable

case class Domain(
  config: DomainConfig,
  loggerFactory: LoggerFactory[Logger],
  services: Adapters
) extends Reusable
