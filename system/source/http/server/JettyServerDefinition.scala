package uk.org.lidalia
package exampleapp.system.http.server

import javax.servlet.http.{HttpServletResponse, HttpServletRequest}

import org.eclipse.jetty.{server => jetty}
import jetty.handler.AbstractHandler
import uk.org.lidalia.http.core.{Response, RequestUri, Method, Request, Http}
import net.{Url, Port}
import uk.org.lidalia.scalalang.ResourceFactory
import uk.org.lidalia.scalalang.ResourceFactory._try

class JettyServerDefinition(
  http: Http[Response],
  port: ?[Port] = None
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
          adaptRequestUri(httpServletRequest),
          List()
        )
        val response = http.execute(request)
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

  def adaptRequestUri[T](httpServletRequest: HttpServletRequest): RequestUri = {
    RequestUri(
      httpServletRequest.getRequestURI +
      (if (httpServletRequest.getQueryString != null) "?" + httpServletRequest.getQueryString else "")
    )
  }
}

class JettyServer private[server] (server: jetty.Server) {
  val baseUrl = Url(server.getURI.toASCIIString)
}
