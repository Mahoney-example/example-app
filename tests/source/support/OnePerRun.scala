package uk.org.lidalia.exampleapp.tests.support

import java.util.concurrent.ConcurrentHashMap

import ch.qos.logback.classic.Level.INFO
import org.scalatest._
import uk.org.lidalia.exampleapp.local.{Environment, EnvironmentDefinition}
import uk.org.lidalia.exampleapp.system.logging.{LogbackLoggerFactory, LogbackLoggingDefinition}
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

trait LogbackLoggerFactoryPerRun extends OnePerRun {

  private val key = "LoggerContext"

  register(key, LogbackLoggingDefinition(
    "uk.org.lidalia" -> INFO
  ))

  def loggerContext(configMap: ConfigMap) = {
    getResourceFactory[LogbackLoggerFactory](configMap, key)
  }
}

trait OnePerRun extends Suite {

  private val factories = new ConcurrentHashMap[String, ResourceFactory[_]]().asScala

  def register(key: String, factory: ResourceFactory[_]) = factories.putIfAbsent(key, factory)

  override def run(testName: Option[String], args: Args): Status = {

    val existing = args.configMap.getWithDefault[ConfigMap]("PerRunResources", ConfigMap())
    val toCreate = factories.filter { pair => !existing.contains(pair._1) }
    ResourceFactory.usingAll(toCreate.values.toSeq:_*) { resources =>
      val created = ConfigMap(factories.keys.zip(resources).toSeq: _*)
      val all = existing.++(created)
      val appendedConfigMap = args.configMap.+("PerRunResources" -> all)
      super.run(testName, args.copy(configMap = appendedConfigMap))
    }
  }

  def getResourceFactory[R](configMap: ConfigMap, key: String) = {
    configMap.getRequired[ConfigMap]("PerRunResources").getRequired[ResourceFactory[R]](key)
  }
}

class SomeTests extends org.scalatest.fixture.FunSuite with EnvironmentDefinitionPerRun with WebDriverDefinitionPerRun with LogbackLoggerFactoryPerRun {

  test("blah") { case (env, driver) =>
    println(env)
    println(driver)
  }

  test("blah2") { case (env, driver) =>
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

class SomeOtherTests extends org.scalatest.fixture.FunSuite with EnvironmentDefinitionPerRun {
  test("blah") { env =>
    println(env)
  }

  test("blah2") { env =>
    println(env)
  }
  override type FixtureParam = Environment

  override protected def withFixture(test: OneArgTest): Outcome = {
    environmentDefinition(test.configMap).using { env =>
      test.apply(env)
    }
  }
}

class SomeSuite extends Suites(
  new SomeTests,
  new SomeOtherTests
) with EnvironmentDefinitionPerRun with WebDriverDefinitionPerRun with LogbackLoggerFactoryPerRun
