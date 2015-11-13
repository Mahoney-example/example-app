package uk.org.lidalia
package exampleapp.server.web

import uk.org.lidalia.exampleapp.system.awaitInterruption
import uk.org.lidalia.exampleapp.server.application.{ApplicationDefinition, ApplicationConfig}
import uk.org.lidalia.scalalang.ResourceFactory
import uk.org.lidalia.net.{Port, Url}

import scala.collection.immutable
import scala.collection.JavaConversions.mapAsScalaMap
import scala.collection.JavaConversions.propertiesAsScalaMap

object ServerDefinition {

  def main(args: Array[String]) {

    val config = configFor(
      args.toVector,
      System.getProperties.toMap,
      System.getenv().toMap
    )

    new ServerDefinition(config).runUntilInterrupted()
  }

  def configFor(
    args: immutable.Seq[String],
    sysProps: Map[String, String],
    env: Map[String, String]
  ): ServerConfig = {

    new ServerConfig (

      new ApplicationConfig(
        sendGridUrl = Url("http://www.example.com"),
        contentfulUrl = Url("http://www.disney.com")
      ),

      localPort = Port(80)
    )
  }
}

class ServerDefinition(
  config: ServerConfig
) extends ResourceFactory[Server] {

  val applicationDefinition = new ApplicationDefinition(
    config.applicationConfig
  )

  def runUntilInterrupted(): Unit = {
    withA(awaitInterruption)
  }

  override def withA[T](work: (Server) => T): T = {

    applicationDefinition.withA { application =>

      val server = new Server(application, config)

      try {
        server.start()
        work(server)
      } finally {
        server.stop()
      }
    }
  }
}
