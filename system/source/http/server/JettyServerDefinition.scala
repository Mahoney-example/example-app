package uk.org.lidalia
package exampleapp.system.http.server

import uk.org.lidalia.http.core.MediaType.`application/octet-stream`
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import org.eclipse.jetty.{server => jetty}
import jetty.handler.AbstractHandler
import uk.org.lidalia.http.core.{EmptyEntity, HeaderField, Http, Method, Request, RequestUri, Response}
import net.{Port, Url}
import org.apache.commons.io.IOUtils
import org.eclipse.jetty.server.ServerConnector
import uk.org.lidalia.exampleapp.system.HasLogger
import uk.org.lidalia.scalalang.ResourceFactory
import uk.org.lidalia.scalalang.TryFinally._try

import scala.collection.JavaConversions.enumerationAsScalaIterator
import scala.collection.JavaConversions.iterableAsScalaIterable

object JettyServerDefinition {
  def apply(http: Http[Response], port: ?[Port] = None) = new JettyServerDefinition(http, port)
}

class JettyServerDefinition private (
  http: Http[Response],
  port: ?[Port]
) extends ResourceFactory[JettyServer] with HasLogger {

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
          RequestUri(baseRequest.getUri.toString),
          baseRequest.getHttpFields.map { field => HeaderField(field.getName, field.getValue) }.toList,
          EmptyEntity
        )
        val response = http.execute(request)
        httpServletResponse.setStatus(response.code.code, response.header.reason.reason)
        response.header.headerFields.foreach { headerField =>
          httpServletResponse.addHeader(headerField.name, headerField.values.mkString(", "))
        }
        IOUtils.copy(
          response.marshallableEntity.marshall(request.contentType.getOrElse(`application/octet-stream`)),
          httpServletResponse.getOutputStream
        )
        baseRequest.setHandled(true)
      }

    })
    _try {
      server.start()
      val jettyServer = new JettyServer(server)
      log.info(s"Server started on ${jettyServer.localPort}")
      work(jettyServer)
    } _finally {
      server.stop()
      server.join()
      log.info(s"Server stopped")
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

  val localPort = Port(server.getConnectors.collect { case s: ServerConnector => s }.head.getLocalPort)
}
