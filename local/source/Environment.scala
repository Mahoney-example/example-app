package uk.org.lidalia.exampleapp.local

import uk.org.lidalia.exampleapp.server.web.Server
import uk.org.lidalia.scalalang.Reusable
import uk.org.lidalia.stubhttp.StubHttpServer

case class Environment (
  stub1: StubHttpServer,
  stub2: StubHttpServer,
  server: Server
) extends Reusable {

  override def reset() = List(stub1, stub2, server).foreach(_.reset())

}
