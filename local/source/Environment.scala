package uk.org.lidalia
package exampleapp.local

import exampleapp.server.adapters.inbound.http.HttpRoutes
import exampleapp.system.db.hsqldb.HsqlDatabase
import scalalang.Reusable
import stubhttp.StubHttpServer

case class Environment (
  stub1: StubHttpServer,
  database: HsqlDatabase,
  servers: List[HttpRoutes]
) extends Reusable {

  override def reset() = (stub1 :: database :: servers).foreach(_.reset())

}
