package uk.org.lidalia.exampleapp.tests.library

import java.util.concurrent.ConcurrentHashMap

import org.scalatest.{Args, ConfigMap, Status, Suite}
import uk.org.lidalia.scalalang.ResourceFactory

import scala.collection.JavaConverters.mapAsScalaConcurrentMapConverter

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
