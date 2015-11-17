package uk.org.lidalia
package exampleapp
package local

import db.changelog.FastLiquibase
import net.Port
import stubhttp.StubHttpServerFactory
import system.db.MemDatabaseDefinition
import system.logging.LogbackConfigurer.configureLogback

object Main {

  configureLogback("uk.org.lidalia.exampleapp")
  FastLiquibase()

  def main(args: Array[String]) {

    val environment = EnvironmentDefinition(
      Port(8080),
      StubHttpServerFactory(Port(8081)),
      MemDatabaseDefinition("local")
    )

    environment.runUntilShutdown()
  }
}
