package uk.org.lidalia
package exampleapp
package server.web

import scalalang.ResourceFactory
import server.application.ApplicationDefinition
import system.blockUntilShutdown

object ServerDefinition {

  def apply(config: ServerConfig) = new ServerDefinition(config)

}

class ServerDefinition private (
  config: ServerConfig
) extends ResourceFactory[Server] {

  val applicationDefinition = ApplicationDefinition(
    config.applicationConfig
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
