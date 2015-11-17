package uk.org.lidalia.exampleapp.local

import uk.org.lidalia.exampleapp.server.web.Server
import uk.org.lidalia.exampleapp.system.db.Database
import uk.org.lidalia.scalalang.Reusable
import uk.org.lidalia.stubhttp.StubHttpServer

case class Environment (
  stub1: StubHttpServer,
  database: Database,
  server1: Server,
  server2: Server
) extends Reusable {

  override def reset() = List(stub1, database, server1, server2).foreach(_.reset())

}
