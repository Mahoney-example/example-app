package uk.org.lidalia
package exampleapp.server

import uk.org.lidalia.exampleapp.server.application.ApplicationDefinition
import uk.org.lidalia.exampleapp.server.web.{Server, ServerConfig, ServerDefinition}
import uk.org.lidalia.exampleapp.system.blockUntilShutdown
import uk.org.lidalia.exampleapp.system.logging.LogbackLoggerFactory
import uk.org.lidalia.scalalang.ResourceFactory

object ProcessDefinition {

  def apply(
    applicationDefinition: ApplicationDefinition,
    serverConfig: ServerConfig
  ): ProcessDefinition = {
    new ProcessDefinition(applicationDefinition, serverConfig)
  }

  def apply(
    loggerFactory: LogbackLoggerFactory,
    config: ServerConfig
  ): ProcessDefinition = {
    ProcessDefinition(
      ApplicationDefinition(
        config.applicationConfig,
        loggerFactory
      ),
      config
    )
  }
}

class ProcessDefinition private (applicationDefinition: ApplicationDefinition, serverConfig: ServerConfig) extends ResourceFactory[Server] {

  def runUntilShutdown(): Unit = {
    using(blockUntilShutdown)
  }

  override def using[T](work: (Server) => T): T = {
    applicationDefinition.using { application =>
      ServerDefinition(serverConfig, application).using { server =>
        work(server)
      }
    }
  }
}
