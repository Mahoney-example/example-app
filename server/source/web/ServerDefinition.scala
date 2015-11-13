package uk.org.lidalia
package exampleapp.server.web

import uk.org.lidalia.exampleapp.system.{JdbcConfig, awaitInterruption}
import uk.org.lidalia.exampleapp.server.application.{ApplicationDefinition, ApplicationConfig}
import uk.org.lidalia.scalalang.ResourceFactory
import uk.org.lidalia.net.{Port, Url}

import scala.collection.immutable
import scala.collection.JavaConversions.mapAsScalaMap
import scala.collection.JavaConversions.propertiesAsScalaMap

object ServerDefinition {

  def apply(config: ServerConfig) = new ServerDefinition(config)

  def main(args: Array[String]) {

    val config = configFor(
      args.toVector,
      System.getProperties.toMap,
      System.getenv().toMap
    )

    ServerDefinition(config).runUntilInterrupted()
  }

  private def configFor(
    args: immutable.Seq[String],
    sysProps: Map[String, String],
    env: Map[String, String]
  ): ServerConfig = {

    ServerConfig (

      ApplicationConfig(
        sendGridUrl = Url("http://www.example.com"),
        sendGridToken = "",
        contentfulUrl = Url("http://www.disney.com"),
        jdbcConfig = JdbcConfig(
          Url(""),
          "sa",
          "",
          "SELECT 1"
        )
      ),

      localPort = Port(80)
    )
  }
}

class ServerDefinition private (
  config: ServerConfig
) extends ResourceFactory[Server] {

  val applicationDefinition = ApplicationDefinition(
    config.applicationConfig
  )

  def runUntilInterrupted(): Unit = {
    using(awaitInterruption)
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
