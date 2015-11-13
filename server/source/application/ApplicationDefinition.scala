package uk.org.lidalia
package exampleapp.server.application

import uk.org.lidalia.exampleapp.server.services.Services
import uk.org.lidalia.exampleapp.server.services.email.HttpEmailService
import uk.org.lidalia.exampleapp.server.services.profiles.DbUserProfileService
import uk.org.lidalia.exampleapp.system.{DatabaseDefinition, HasLogger}
import uk.org.lidalia.scalalang.ResourceFactory

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

    services match {
      case None =>
        DatabaseDefinition(config.jdbcConfig).using { database =>
          val application = Application(
            config,
            Services(
              HttpEmailService(config.sendGridUrl, config.sendGridToken),
              DbUserProfileService(database)
            )
          )

          run(work, application)
        }
      case Some(servs) =>
        val application = Application(config, servs)
        run(work, application)
    }
  }

  private def run[T](work: (Application) => T, application: Application): T = {
    try {
      log.info(s"Application started: $this")
      work(application)
    } finally {
      log.info(s"Application stopped: $this")
    }
  }
}
