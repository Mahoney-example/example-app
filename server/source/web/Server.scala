package uk.org.lidalia.exampleapp.server.web

import uk.org.lidalia.exampleapp.system.HasLogger
import uk.org.lidalia.exampleapp.server.application.Application
import uk.org.lidalia.exampleapp.system.http.server.JettyServer
import uk.org.lidalia.scalalang.Reusable

case class Server private[web] (
  application: Application,
  jettyServer: JettyServer
) extends Reusable with HasLogger {

  def localPort = jettyServer.localPort

  override def reset(): Unit = application.reset()

}
