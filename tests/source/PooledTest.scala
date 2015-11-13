package uk.org.lidalia.exampleapp.tests.functional

import uk.org.lidalia.exampleapp.local.EnvironmentDefinition

object PooledTest {

  def main(args: Array[String]) {

    EnvironmentDefinition.withA { environment =>
      // do whatever here
    }
  }
}
