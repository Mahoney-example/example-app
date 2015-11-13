package uk.org.lidalia.exampleapp.server.application

import uk.org.lidalia.exampleapp.server.services.Services
import uk.org.lidalia.scalalang.Reusable

case class Application(
  config: ApplicationConfig,
  services: Services
) extends Reusable
