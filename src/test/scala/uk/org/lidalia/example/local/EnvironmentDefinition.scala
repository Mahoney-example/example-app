package uk.org.lidalia.example.local

import uk.org.lidalia.example
import example.server.application.ApplicationConfig
import example.server.web.{ServerDefinition, ServerConfig}
import example.system.awaitInterruption

import uk.org.lidalia.lang.ResourceFactory
import ResourceFactory.withAll

import uk.org.lidalia.stubhttp.StubHttpServerFactory

object EnvironmentDefinition extends ResourceFactory[Environment] {

  private val stub1Definition = StubHttpServerFactory()
  private val stub2Definition = StubHttpServerFactory()

  def main(args: Array[String]) {
    withA(awaitInterruption)
  }

  override def withA[T](work: (Environment) => T): T = {

    withAll(stub1Definition, stub2Definition) { (stub1, stub2) =>

      val config = ServerConfig(
        ApplicationConfig(
          sendGridUrl = stub1.localAddress,
          contentfulUrl = stub2.localAddress
        ),
        localPort = None
      )

      new ServerDefinition(config).withA { application =>
        work(new Environment(stub1, stub2, application))
      }
    }
  }
}
