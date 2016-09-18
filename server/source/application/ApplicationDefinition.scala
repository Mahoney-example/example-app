package uk.org.lidalia
package exampleapp
package server.application

import org.slf4j.Logger
import scalalang.ResourceFactory
import server.services.Services
import server.services.email.HttpEmailService
import server.services.profiles.DbUserProfileService
import system.HasLogger
import system.db.PooledDatabaseDefinition
import system.logging.{StaticLoggerFactory, LoggerFactory}

object ApplicationDefinition {
  def apply(
    config: ApplicationConfig,
    loggerFactory: LoggerFactory[Logger] = StaticLoggerFactory
  ) = {
    new ApplicationDefinition(config, loggerFactory)
  }
}

class ApplicationDefinition private (
  config: ApplicationConfig,
  loggerFactory: LoggerFactory[Logger]
) extends ResourceFactory[Application] with HasLogger {

  override def using[T](work: (Application) => T): T = {

    PooledDatabaseDefinition(config.jdbcConfig).using { database =>
      val services = Services(
        HttpEmailService(config.sendGridUrl, config.sendGridToken),
        DbUserProfileService(database)
      )
      val application = Application(config, loggerFactory, services)
      work(application)
    }
  }
}
