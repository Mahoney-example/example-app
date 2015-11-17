package uk.org.lidalia.exampleapp.tests.functional

import db.changelog.FastLiquibase
import uk.org.lidalia.exampleapp.local.EnvironmentDefinition
import uk.org.lidalia.exampleapp.system.HasLogger
import uk.org.lidalia.exampleapp.system.logging.JuliConfigurer

object Test extends HasLogger {

  JuliConfigurer.sendJuliToSlf4j()
  FastLiquibase

  def main(args: Array[String]) {

    val start = System.currentTimeMillis()
    EnvironmentDefinition().using { environment =>
      log.info(s"Running a test against server on port ${environment.server.localPort} $environment")
    }
    println("Done in "+(System.currentTimeMillis() - start))
  }
}
