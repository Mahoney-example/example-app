package uk.org.lidalia.exampleapp.tests.support

import java.util.concurrent.ConcurrentHashMap

import org.scalatest._
import uk.org.lidalia.exampleapp.local.{Environment, EnvironmentDefinition}
import uk.org.lidalia.scalalang.{PoolFactory, ResourceFactory}

import scala.collection.JavaConverters._

trait EnvironmentDefinitionPerRun extends OnePerRun {

  private val key = "EnvironmentDefinition"

  register(key, PoolFactory(EnvironmentDefinition()))

  def environmentDefinition(configMap: ConfigMap) = {
    getResourceFactory[Environment](configMap, key)
  }
}

trait WebDriverDefinitionPerRun extends OnePerRun {

  private val key = "WebDriverDefinition"

  register(key, PoolFactory(WebDriverDefinition()))

  def webDriverDefinition(configMap: ConfigMap) = {
    getResourceFactory[ReusableWebDriver](configMap, key)
  }
}

trait OnePerRun extends Suite {

  private val factoryRegister = new ConcurrentHashMap[String, ResourceFactory[_]]().asScala

  def register(key: String, factory: ResourceFactory[_]) = factoryRegister.putIfAbsent(key, factory)

  override def run(testName: Option[String], args: Args): Status = {

    if (args.configMap.contains("PerRunResources")) {
      super.run(testName, args)
    } else {
      ResourceFactory.usingAll(factoryRegister.values.toSeq:_*) { resources =>
        val appendedConfigMap = args.configMap.+("PerRunResources" -> ConfigMap(factoryRegister.keys.zip(resources).toSeq:_*))
        super.run(testName, args.copy(configMap = appendedConfigMap))
      }
    }
  }

  def getResourceFactory[R](configMap: ConfigMap, key: String) = {
    configMap.getRequired[ConfigMap]("PerRunResources").getRequired[ResourceFactory[R]](key)
  }
}

class SomeTests extends org.scalatest.fixture.FunSuite with EnvironmentDefinitionPerRun with WebDriverDefinitionPerRun {

  test("blah") { case (env, driver) =>
    println(env)
    println(driver)
  }
  override type FixtureParam = (Environment, ReusableWebDriver)

  override protected def withFixture(test: OneArgTest): Outcome = {
    ResourceFactory.usingAll(environmentDefinition(test.configMap), webDriverDefinition(test.configMap)) { (env, driver) =>
      test.apply((env, driver))
    }
  }
}
