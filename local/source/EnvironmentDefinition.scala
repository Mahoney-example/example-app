package uk.org.lidalia
package exampleapp
package local

import org.slf4j.Logger
import server.application.ApplicationConfig
import server.services.profiles.userProfileTableCreation
import server.web.{ServerDefinition, ServerConfig}
import system.db.changelog.Migrator.changeLog
import system.db.{Database, MemDatabaseDefinition, DatabaseDefinition}
import system.blockUntilShutdown
import net.Port

import scalalang.ResourceFactory
import ResourceFactory.usingAll
import system.logging.{StaticLoggerFactory, LoggerFactory}

import uk.org.lidalia.stubhttp.StubHttpServerFactory

object EnvironmentDefinition {

  def apply(
    ports: List[?[Port]] = List(None),
    loggerFactory: LoggerFactory[Logger] = StaticLoggerFactory,
    stub1Definition: StubHttpServerFactory = StubHttpServerFactory(),
    databaseDefinition: DatabaseDefinition = MemDatabaseDefinition()
  ) = {

    val initialisingDbDefinition = new DatabaseDefinition {
      override def using[T](work: (Database) => T): T = {
        databaseDefinition.using { database =>
          database.update(changeLog(userProfileTableCreation))
          work(database)
        }
      }
    }

    new EnvironmentDefinition(
      ports,
      loggerFactory,
      stub1Definition,
      initialisingDbDefinition
    )
  }
}

class EnvironmentDefinition private (
  ports: List[?[Port]],
  loggerFactory: LoggerFactory[Logger],
  stub1Definition: StubHttpServerFactory,
  databaseDefinition: DatabaseDefinition
) extends ResourceFactory[Environment] {

  def runUntilShutdown(): Unit = {
    using(blockUntilShutdown)
  }

  override def using[T](work: (Environment) => T): T = {

    usingAll(
      stub1Definition,
      databaseDefinition
    ) { (stub1, database) =>

      val appConfig = ApplicationConfig(
        sendGridUrl = stub1.localAddress,
        sendGridToken = "secret_token",
        jdbcConfig = database.jdbcConfig
      )

      val serverDefinitions = ports.map(port => ServerDefinition(ServerConfig(appConfig, port), loggerFactory))

      usingAll(serverDefinitions:_*) { servers =>
        work(Environment(
          stub1,
          database,
          servers
        ))
      }
    }
  }
}
