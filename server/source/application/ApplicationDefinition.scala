package uk.org.lidalia
package exampleapp
package server.application

import liquibase.changelog.DatabaseChangeLog
import org.slf4j.Logger
import scalalang.ResourceFactory
import scalalang.ResourceFactory._try
import server.services.Services
import server.services.email.HttpEmailService
import server.services.profiles.{userProfileTableCreation, DbUserProfileService}
import system.HasLogger
import system.db.PooledDatabaseDefinition
import system.db.changelog.Migrator.changeLog
import system.logging.{StaticLoggerFactory, LoggerFactory}

object ApplicationDefinition {
  def apply(
    config: ApplicationConfig,
    loggerFactory: LoggerFactory[Logger] = StaticLoggerFactory,
    services: ?[Services] = None
  ) = {
    new ApplicationDefinition(config, loggerFactory, services)
  }
}

class ApplicationDefinition private (
  config: ApplicationConfig,
  loggerFactory: LoggerFactory[Logger],
  services: ?[Services]
) extends ResourceFactory[Application] with HasLogger {

  override def using[T](work: (Application) => T): T = {

    def run(servs: Services): T = {
      val application = Application(config, loggerFactory, servs)
      _try {
        log.info(s"Application started: $application")
        work(application)
      } _finally {
        log.info(s"Application stopped: $application")
      }
    }

    services match {
      case None =>
        PooledDatabaseDefinition(config.jdbcConfig, changeLog(userProfileTableCreation)).using { database =>
          run(Services(
            HttpEmailService(config.sendGridUrl, config.sendGridToken),
            DbUserProfileService(database)
          ))
        }
      case Some(servs) => run(servs)
    }
  }
}
