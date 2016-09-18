package uk.org.lidalia
package exampleapp
package server.web

import scalalang.ResourceFactory
import scalalang.TryFinally._try
import server.application.Application
import uk.org.lidalia.exampleapp.system.http.server.JettyServerDefinition
import uk.org.lidalia.http.core.{Code, Entity, HeaderField, Http, Request, Response, StringEntity}

object ServerDefinition {

  def apply(
    config: ServerConfig,
    application: Application
  ) = new ServerDefinition(config, application)

}

class ServerDefinition private (
  config: ServerConfig,
  application: Application
) extends ResourceFactory[Server] {

  override def using[T](work: (Server) => T): T = {

    val domainWithHttpPorts = toHttp(application)

    JettyServerDefinition(domainWithHttpPorts, config.localPort).using { jettyServer =>
      work(Server(application, jettyServer))
    }
  }

  private def toHttp(application: Application): Http[Response] = new Http[Response] {
    override def execute[A, C](request: Request[A, C]) = Response(
      Code.OK,
      List(HeaderField("Foo", "foo1"), HeaderField("Foo", "foo2")),
      new StringEntity("Hello World!\nGood to see you\n").asInstanceOf[Entity[A]]
    )
  }
}
