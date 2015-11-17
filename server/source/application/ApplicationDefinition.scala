package uk.org.lidalia
package exampleapp
package server.application

import scalalang.ResourceFactory
import server.services.Services
import server.services.email.HttpEmailService
import server.services.profiles.DbUserProfileService
import system.HasLogger
import system.db.PooledDatabaseDefinition

object ApplicationDefinition {
  def apply(
    config: ApplicationConfig,
    services: ?[Services] = None
  ) = {
    new ApplicationDefinition(config, services)
  }
}

class ApplicationDefinition private (
  config: ApplicationConfig,
  services: ?[Services]
) extends ResourceFactory[Application] with HasLogger {

  override def using[T](work: (Application) => T): T = {

    def run(servs: Services): T = {
      val application = Application(config, servs)
      try {
        log.info(s"Application started: $application")
        work(application)
      } finally {
        log.info(s"Application stopped: $application")
      }
    }

    services match {
      case None =>
        PooledDatabaseDefinition(config.jdbcConfig).using { database =>
          run(Services(
            HttpEmailService(config.sendGridUrl, config.sendGridToken),
            DbUserProfileService(database)
          ))
        }
      case Some(servs) => run(servs)
    }
  }
}
