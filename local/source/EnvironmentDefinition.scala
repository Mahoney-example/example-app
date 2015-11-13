package uk.org.lidalia.exampleapp.local

import org.apache.commons.lang3.RandomStringUtils
import uk.org.lidalia.exampleapp
import exampleapp.server.application.ApplicationConfig
import exampleapp.server.web.{ServerDefinition, ServerConfig}
import uk.org.lidalia.exampleapp.system.{JdbcConfig, awaitInterruption}
import uk.org.lidalia.net.{Uri, Url, Port}

import uk.org.lidalia.scalalang.ResourceFactory
import ResourceFactory.withAll

import uk.org.lidalia.stubhttp.StubHttpServerFactory

object EnvironmentDefinition {

  def apply(
    stub1Definition: StubHttpServerFactory = StubHttpServerFactory(),
    stub2Definition: StubHttpServerFactory = StubHttpServerFactory(),
    jdbcConfig: JdbcConfig = dbConfig()) = {
    new EnvironmentDefinition(
      stub1Definition,
      stub2Definition,
      jdbcConfig
    )
  }

  def main(args: Array[String]) {
    apply(
      StubHttpServerFactory(Port(8081)),
      StubHttpServerFactory(Port(8082)),
      dbConfig("local")
    ).withA(awaitInterruption)
  }

  def dbConfig(dbName: String = RandomStringUtils.randomAlphanumeric(5)) = JdbcConfig(
    Uri(s"jdbc:hsqldb:mem:$dbName"),
    "sa",
    "",
    "SELECT 1"
  )
}

class EnvironmentDefinition private (
  stub1Definition: StubHttpServerFactory,
  stub2Definition: StubHttpServerFactory,
  jdbcConfig: JdbcConfig
) extends ResourceFactory[Environment] {

  override def withA[T](work: (Environment) => T): T = {

    withAll(stub1Definition, stub2Definition) { (stub1, stub2) =>

      val config = ServerConfig(
        ApplicationConfig(
          sendGridUrl = stub1.localAddress,
          sendGridToken = "secret_token",
          contentfulUrl = stub2.localAddress,
          jdbcConfig = jdbcConfig
        ),
        localPort = None
      )

      ServerDefinition(config).withA { application =>
        work(Environment(stub1, stub2, application))
      }
    }
  }
}
