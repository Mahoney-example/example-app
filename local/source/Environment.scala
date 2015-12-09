package uk.org.lidalia.exampleapp.local

import uk.org.lidalia.exampleapp.server.web.Server
import uk.org.lidalia.exampleapp.system.db.hsqldb.HsqlDatabase
import uk.org.lidalia.scalalang.Reusable
import uk.org.lidalia.stubhttp.StubHttpServer

case class Environment (
  stub1: StubHttpServer,
  database: HsqlDatabase,
  servers: List[Server]
) extends Reusable {

  override def reset() = (stub1 :: database :: servers).foreach(_.reset())

}
