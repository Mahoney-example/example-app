package uk.org.lidalia
package exampleapp.server

import exampleapp.server.adapters.http.{HttpRoutes, HttpRoutesConfig, HttpRoutesDefinition}
import exampleapp.server.domain.DomainDefinition
import exampleapp.system.blockUntilShutdown
import exampleapp.system.logging.LogbackLoggerFactory
import scalalang.ResourceFactory

object ServerDefinition {

  def apply(
             domainDefinition: DomainDefinition,
             portsConfig: HttpRoutesConfig
  ): ServerDefinition = {
    new ServerDefinition(domainDefinition, portsConfig)
  }

  def apply(
    loggerFactory: LogbackLoggerFactory,
    config: HttpRoutesConfig
  ): ServerDefinition = {
    ServerDefinition(
      DomainDefinition(
        config.domainConfig,
        loggerFactory
      ),
      config
    )
  }
}

class ServerDefinition private(domainDefinition: DomainDefinition, portsConfig: HttpRoutesConfig) extends ResourceFactory[HttpRoutes] {

  def runUntilShutdown(): Unit = {
    using(blockUntilShutdown)
  }

  override def using[T](work: (HttpRoutes) => T): T = {
    domainDefinition.using { domain =>
      HttpRoutesDefinition(portsConfig, domain).using { ports =>
        work(ports)
      }
    }
  }
}
