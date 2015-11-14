package uk.org.lidalia
package exampleapp
package local

import org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric
import server.application.ApplicationConfig
import server.web.{ServerDefinition, ServerConfig}
import uk.org.lidalia.exampleapp.system.{HsqlDatabaseDefinition, blockUntilShutdown, DatabaseDefinition, JdbcConfig}
import net.{Port, Uri}

import scalalang.ResourceFactory
import ResourceFactory.withAll

import uk.org.lidalia.stubhttp.StubHttpServerFactory

object EnvironmentDefinition {

  def apply(
    port: ?[Port] = None,
    stub1Definition: StubHttpServerFactory = StubHttpServerFactory(),
    stub2Definition: StubHttpServerFactory = StubHttpServerFactory(),
    databaseDefinition: HsqlDatabaseDefinition = HsqlDatabaseDefinition()
  ) = {
    new EnvironmentDefinition(
      port,
      stub1Definition,
      stub2Definition,
      databaseDefinition
    )
  }
}

class EnvironmentDefinition private (
  port: ?[Port],
  stub1Definition: StubHttpServerFactory,
  stub2Definition: StubHttpServerFactory,
  databaseDefinition: DatabaseDefinition
) extends ResourceFactory[Environment] {

  def runUntilShutdown(): Unit = {
    using(blockUntilShutdown)
  }

  override def using[T](work: (Environment) => T): T = {

    withAll(
      stub1Definition,
      stub2Definition,
      databaseDefinition
    ) { (stub1, stub2, database) =>

      val config = ServerConfig(
        ApplicationConfig(
          sendGridUrl = stub1.localAddress,
          sendGridToken = "secret_token",
          contentfulUrl = stub2.localAddress,
          jdbcConfig = databaseDefinition.config
        ),
        localPort = port
      )

      ServerDefinition(config).using { application =>
        work(Environment(stub1, stub2, database, application))
      }
    }
  }
}
