package uk.org.lidalia
package exampleapp.local

import org.slf4j.Logger
import exampleapp.server.domain.{DomainConfig, DomainDefinition}
import exampleapp.server.adapters.outbound.profiles.userProfileTableCreation
import exampleapp.system.db.changelog.Migrator.changeLog
import exampleapp.system.db.hsqldb.{HsqlDatabase, HsqlDatabaseDefinition}
import exampleapp.system.blockUntilShutdown
import net.Port
import scalalang.ResourceFactory
import ResourceFactory.usingAll
import exampleapp.system.logging.{LoggerFactory, StaticLoggerFactory}
import exampleapp.server.ServerDefinition
import exampleapp.server.adapters.http.HttpRoutesConfig
import stubhttp.StubHttpServerFactory

object EnvironmentDefinition {

  def apply(
    ports: List[?[Port]] = List(None),
    loggerFactory: LoggerFactory[Logger] = StaticLoggerFactory,
    stub1Definition: StubHttpServerFactory = StubHttpServerFactory(),
    databaseDefinition: ResourceFactory[HsqlDatabase] = HsqlDatabaseDefinition(changeLog(userProfileTableCreation))
  ) = {
    new EnvironmentDefinition(
      ports,
      loggerFactory,
      stub1Definition,
      databaseDefinition
    )
  }
}

class EnvironmentDefinition private (
  ports: List[?[Port]],
  loggerFactory: LoggerFactory[Logger],
  stub1Definition: StubHttpServerFactory,
  databaseDefinition: ResourceFactory[HsqlDatabase]
) extends ResourceFactory[Environment] {

  def runUntilShutdown(): Unit = {
    using(blockUntilShutdown)
  }

  override def using[T](work: (Environment) => T): T = {

    usingAll(
      stub1Definition,
      databaseDefinition
    ) { (stub1, database) =>

      database.update()

      val appConfig = DomainConfig(
        sendGridUrl = stub1.localAddress,
        sendGridToken = "secret_token",
        jdbcConfig = database.jdbcConfig
      )

      val serverDefinitions = ports.map { port =>
        ServerDefinition(DomainDefinition(appConfig, loggerFactory), HttpRoutesConfig(appConfig, port))
      }

      usingAll(serverDefinitions:_*) { servers =>
        work(Environment(
          stub1,
          database,
          servers.toList
        ))
      }
    }
  }
}
