package uk.org.lidalia
package exampleapp.server.adapters.outbound

import scalalang.ResourceFactory
import uk.org.lidalia.exampleapp.server.adapters.outbound.email.HttpEmailService
import uk.org.lidalia.exampleapp.server.adapters.outbound.profiles.DbUserProfileRepository
import uk.org.lidalia.exampleapp.server.domain.OutboundAdapters
import uk.org.lidalia.exampleapp.system.db.PooledDatabaseDefinition

class OutboundAdaptersDefinition(config: OutboundAdaptersConfig) extends ResourceFactory[OutboundAdapters] {

  override def using[T](work: (OutboundAdapters) => T): T = {

    PooledDatabaseDefinition(config.jdbcConfig).using { database =>
      val adapters = OutboundAdapters(
        HttpEmailService(config.sendGridUrl, config.sendGridToken),
        DbUserProfileRepository(database)
      )
      work(adapters)
    }
  }

}
