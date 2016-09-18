package uk.org.lidalia
package exampleapp.server.adapters.http

import exampleapp.server.domain.Domain
import exampleapp.system.http.server.JettyServerDefinition
import http.core.{Code, Entity, HeaderField, Http, Request, Response, StringEntity}
import scalalang.ResourceFactory

object HttpRoutesDefinition {

  def apply(
    config: HttpRoutesConfig,
    domain: Domain
  ) = new HttpRoutesDefinition(config, domain)

}

class HttpRoutesDefinition private(
  config: HttpRoutesConfig,
  domain: Domain
) extends ResourceFactory[HttpRoutes] {

  override def using[T](work: (HttpRoutes) => T): T = {

    val domainWithHttpRoutes = toHttp(domain)

    JettyServerDefinition(domainWithHttpRoutes, config.localPort).using { jettyServer =>
      work(HttpRoutes(domain, jettyServer))
    }
  }

  private def toHttp(domain: Domain): Http[Response] = new Http[Response] {
    override def execute[A, C](request: Request[A, C]) = Response(
      Code.OK,
      List(HeaderField("Foo", "foo1"), HeaderField("Foo", "foo2")),
      new StringEntity("Hello World!\nGood to see you\n").asInstanceOf[Entity[A]]
    )
  }
}
