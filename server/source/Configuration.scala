package uk.org.lidalia
package exampleapp.server

import exampleapp.server.adapters.inbound.http.HttpRoutesConfig
import exampleapp.system.db.JdbcConfig
import net.{Port, Uri, Url}
import uk.org.lidalia.exampleapp.server.adapters.outbound.OutboundAdaptersConfig
import uk.org.lidalia.exampleapp.server.domain.DomainConfig

import scala.collection.immutable

object Configuration {

  def apply(
    args: immutable.Seq[String],
    sysProps: Map[String, String],
    env: Map[String, String]
  ): Configuration = {
    new Configuration(
      HttpRoutesConfig(
        Port(8080)
      ),
      OutboundAdaptersConfig(
        sendGridUrl = Url("http://www.example.com"),
        sendGridToken = "",
        jdbcConfig = JdbcConfig(
          Uri("jdbc:postgresql:local"),
          "sa",
          "",
          "SELECT 1"
        )
      ),
      DomainConfig()
    )
  }
}

case class Configuration(

  httpRoutesConfig: HttpRoutesConfig,

  adaptersConfig: OutboundAdaptersConfig,

  domainConfig: DomainConfig

)
