package uk.org.lidalia
package exampleapp.server

import exampleapp.server.adapters.http.{HttpRoutes, HttpRoutesDefinition}
import exampleapp.server.adapters.outbound.OutboundAdaptersDefinition
import exampleapp.system.blockUntilShutdown
import exampleapp.system.logging.LoggerFactory
import org.slf4j.Logger
import scalalang.ResourceFactory
import uk.org.lidalia.exampleapp.server.domain.Domain

object ServerDefinition {

  def apply(
    loggerFactory: LoggerFactory[Logger],
    config: Configuration
  ): ServerDefinition = {

    val adapters = new OutboundAdaptersDefinition(
      config.adaptersConfig
    )

    new ServerDefinition(
      adapters,
      config,
      loggerFactory
    )
  }
}

class ServerDefinition private(

  adaptersDefinition: OutboundAdaptersDefinition,
  config: Configuration,
  loggerFactory: LoggerFactory[Logger]

) extends ResourceFactory[HttpRoutes] {

  def runUntilShutdown(): Unit = {
    using(blockUntilShutdown)
  }

  override def using[T](work: (HttpRoutes) => T): T = {

    adaptersDefinition.using { adapters =>

      val domain = Domain(config.domainConfig, loggerFactory, adapters)

      val httpRoutesDefinition = HttpRoutesDefinition(config.httpRoutesConfig, domain)

      httpRoutesDefinition.using { httpRoutes =>
        work(httpRoutes)
      }
    }
  }
}
