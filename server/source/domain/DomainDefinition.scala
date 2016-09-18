package uk.org.lidalia
package exampleapp.server.domain

import org.slf4j.Logger
import scalalang.ResourceFactory
import exampleapp.server.adapters.outbound.profiles.DbUserProfileService
import exampleapp.system.HasLogger
import exampleapp.system.db.PooledDatabaseDefinition
import exampleapp.system.logging.{LoggerFactory, StaticLoggerFactory}
import exampleapp.server.adapters.outbound.Adapters
import exampleapp.server.adapters.outbound.email.HttpEmailService

object DomainDefinition {
  def apply(
    config: DomainConfig,
    loggerFactory: LoggerFactory[Logger] = StaticLoggerFactory
  ) = {
    new DomainDefinition(config, loggerFactory)
  }
}

class DomainDefinition private(
  config: DomainConfig,
  loggerFactory: LoggerFactory[Logger]
) extends ResourceFactory[Domain] with HasLogger {

  override def using[T](work: (Domain) => T): T = {

    PooledDatabaseDefinition(config.jdbcConfig).using { database =>
      val services = Adapters(
        HttpEmailService(config.sendGridUrl, config.sendGridToken),
        DbUserProfileService(database)
      )
      val application = Domain(config, loggerFactory, services)
      work(application)
    }
  }
}
