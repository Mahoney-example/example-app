package uk.org.lidalia
package exampleapp.server.domain

import org.slf4j.Logger
import exampleapp.system.logging.LoggerFactory
import scalalang.Reusable

case class Domain(
  config: DomainConfig,
  loggerFactory: LoggerFactory[Logger],
  adapters: OutboundAdapters
) extends Reusable
