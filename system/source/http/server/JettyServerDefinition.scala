package uk.org.lidalia
package exampleapp.system.http.server

import javax.servlet.http.{HttpServletResponse, HttpServletRequest}

import org.eclipse.jetty.{server => jetty}
import jetty.handler.AbstractHandler
import uk.org.lidalia.http.core.{RequestUri, Method, Request, Http}
import uk.org.lidalia.net.Port
import uk.org.lidalia.scalalang.ResourceFactory
import uk.org.lidalia.scalalang.ResourceFactory._try

import scala.concurrent.Future

class JettyServerDefinition(
  port: ?[Port],
  http: Http[Future]
) extends ResourceFactory[JettyServer]{

  override def using[T](work: (JettyServer) => T): T = {

    val server: jetty.Server = new jetty.Server(port.map(_.portNumber).getOrElse(0))
    server.setHandler(new AbstractHandler {

      override def handle(
        target: String,
        baseRequest: jetty.Request,
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse
      ): Unit = {
        val request = Request(
          Method(baseRequest.getMethod),
          RequestUri(baseRequest.getPathInfo),
          httpServletRequest.getH
        )
        val response = http.execute()
      }

    })
    val jettyServer = new JettyServer(server)
    server.start()
    _try {
      work(jettyServer)
    } _finally {
      server.stop()
      server.join()
    }
  }
}

class JettyServer private (server: Server) {

}
