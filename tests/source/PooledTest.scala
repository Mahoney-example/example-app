package uk.org.lidalia.exampleapp.tests.functional

import uk.org.lidalia.exampleapp.local.EnvironmentDefinition

object PooledTest {

  def main(args: Array[String]) {

    EnvironmentDefinition().using { environment =>
      // do whatever here
    }
  }
}
