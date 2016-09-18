package uk.org.lidalia
package exampleapp.server.adapters.outbound

import net.Url

case class AdaptersConfig(
  sendGridUrl: Url,
  contentfulUrl: Url
)
