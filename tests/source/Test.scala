package uk.org.lidalia.exampleapp.tests.functional

import uk.org.lidalia.exampleapp.local.EnvironmentDefinition
import uk.org.lidalia.exampleapp.system.HasLogger
import uk.org.lidalia.slf4jext.Level
import uk.org.lidalia.slf4jtest.TestLoggerFactory

object Test extends HasLogger {

//  TestLoggerFactory.getInstance().setPrintLevel(Level.TRACE)

  def main(args: Array[String]) {

//    Range(0, 999).foreach(i => {
//      System.err.println(s"Run $i")
      EnvironmentDefinition().using { environment =>
        log.info(s"Running a test against server on port ${environment.server.localPort} $environment")
      }
    System.gc()
    Thread.sleep(2000L)
//    })
  }
}
