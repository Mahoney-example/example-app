package uk.org.lidalia
package exampleapp.server

import exampleapp.server.adapters.http.HttpRoutesConfig
import exampleapp.server.domain.DomainConfig
import exampleapp.system.db.JdbcConfig
import net.{Port, Uri, Url}

import scala.collection.immutable

object Configuration {

  def apply(
    args: immutable.Seq[String],
    sysProps: Map[String, String],
    env: Map[String, String]
  ): HttpRoutesConfig = {
    HttpRoutesConfig (
      DomainConfig(
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
