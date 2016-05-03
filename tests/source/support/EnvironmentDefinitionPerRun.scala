package uk.org.lidalia.exampleapp.tests.support

import org.scalatest.ConfigMap
import uk.org.lidalia.exampleapp.local.{Environment, EnvironmentDefinition}
import uk.org.lidalia.exampleapp.tests.library.OnePerRun
import uk.org.lidalia.scalalang.PoolFactory

trait EnvironmentDefinitionPerRun extends OnePerRun {

  private val key = "EnvironmentDefinition"

  register(key, PoolFactory(EnvironmentDefinition()))

  def environmentDefinition(configMap: ConfigMap) = {
    getResourceFactory[Environment](configMap, key)
  }
}
