package uk.org.lidalia.exampleapp.server.application

import org.slf4j.Logger
import uk.org.lidalia.exampleapp.server.services.Services
import uk.org.lidalia.exampleapp.system.logging.LoggerFactory
import uk.org.lidalia.scalalang.Reusable

case class Application(
  config: ApplicationConfig,
  loggerFactory: LoggerFactory[Logger],
  services: Services
) extends Reusable
