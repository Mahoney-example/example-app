package uk.org.lidalia.exampleapp.server.application

import uk.org.lidalia.net.Url

case class ApplicationConfig (
  sendGridUrl: Url,
  contentfulUrl: Url
)
