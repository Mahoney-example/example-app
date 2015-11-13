package uk.org.lidalia
package exampleapp
package local

import net.Port
import stubhttp.StubHttpServerFactory

object Main {

  def main(args: Array[String]) {

    val environment = EnvironmentDefinition(
      Port(8080),
      StubHttpServerFactory(Port(8081)),
      StubHttpServerFactory(Port(8082)),
      "local"
    )

    environment.runUntilShutdown()
  }
}
