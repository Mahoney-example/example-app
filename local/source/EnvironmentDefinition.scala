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
    port1: ?[Port] = None,
    loggerFactory1: LoggerFactory[Logger] = StaticLoggerFactory,
    port2: ?[Port] = None,
    loggerFactory2: LoggerFactory[Logger] = StaticLoggerFactory,
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
      port1,
      loggerFactory1,
      port2,
      loggerFactory2,
      stub1Definition,
      initialisingDbDefinition
    )
  }
}

class EnvironmentDefinition private (
  port1: ?[Port],
  loggerFactory1: LoggerFactory[Logger],
  port2: ?[Port],
  loggerFactory2: LoggerFactory[Logger],
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
      val config1 = ServerConfig(
        appConfig,
        localPort = port1
      )
      val config2 = ServerConfig(
        appConfig,
        localPort = port2
      )

      usingAll(ServerDefinition(config1, loggerFactory1), ServerDefinition(config2, loggerFactory2)) { (server1, server2) =>
        work(Environment(
          stub1,
          database,
          server1,
          server2
        ))
      }
    }
  }
}
