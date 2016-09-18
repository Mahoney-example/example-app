package uk.org.lidalia
package exampleapp.server.adapters.http

import exampleapp.server.domain.Domain
import exampleapp.system.HasLogger
import exampleapp.system.http.server.JettyServer
import scalalang.Reusable

case class HttpRoutes private[http](
  application: Domain,
  jettyServer: JettyServer
) extends Reusable with HasLogger {

  def localPort = jettyServer.localPort

  override def reset(): Unit = application.reset()

}
