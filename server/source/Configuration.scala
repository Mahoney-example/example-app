package uk.org.lidalia
package exampleapp

import uk.org.lidalia.exampleapp.server.application.ApplicationConfig
import uk.org.lidalia.exampleapp.server.web.ServerConfig
import uk.org.lidalia.exampleapp.system.db.JdbcConfig
import uk.org.lidalia.net.{Port, Uri, Url}

import scala.collection.immutable

object Configuration {

  def apply(
    args: immutable.Seq[String],
    sysProps: Map[String, String],
    env: Map[String, String]
  ): ServerConfig = {
    ServerConfig (
      ApplicationConfig(
        sendGridUrl = Url("http://www.example.com"),
        sendGridToken = "",
        jdbcConfig = JdbcConfig(
          Uri("jdbc:postgresql:local"),
          "sa",
          "",
          "SELECT 1"
        )
      ),
      localPort = Port(8080)
    )
  }
}
