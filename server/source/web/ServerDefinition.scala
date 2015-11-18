package uk.org.lidalia
package exampleapp
package server.web

import org.slf4j.Logger
import scalalang.ResourceFactory
import server.application.ApplicationDefinition
import system.blockUntilShutdown
import system.logging.LoggerFactory

object ServerDefinition {

  def apply(
    config: ServerConfig,
    loggerFactory: LoggerFactory[Logger]
  ) = new ServerDefinition(config, loggerFactory)

}

class ServerDefinition private (
  config: ServerConfig,
  loggerFactory: LoggerFactory[Logger]
) extends ResourceFactory[Server] {

  val applicationDefinition = ApplicationDefinition(
    config.applicationConfig,
    loggerFactory
  )

  def runUntilShutdown(): Unit = {
    using(blockUntilShutdown)
  }

  override def using[T](work: (Server) => T): T = {

    applicationDefinition.using { application =>

      val server = Server(application, config)

      try {
        server.start()
        work(server)
      } finally {
        server.stop()
      }
    }
  }
}
