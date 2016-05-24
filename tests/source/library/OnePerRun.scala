package uk.org.lidalia.exampleapp.tests.library

import java.util.concurrent.ConcurrentHashMap

import org.scalatest.{Args, ConfigMap, Status, Suite}
import uk.org.lidalia.scalalang.ResourceFactory

import scala.collection.JavaConverters.mapAsScalaConcurrentMapConverter

trait OnePerRun extends Suite {

  private val serialFactories = new ConcurrentHashMap[String, ResourceFactory[_]]().asScala
  private val parallelFactories = new ConcurrentHashMap[String, ResourceFactory[_]]().asScala

  def registerSerial(key: String, factory: ResourceFactory[_]) = serialFactories.putIfAbsent(key, factory)
  def register(key: String, factory: ResourceFactory[_]) = parallelFactories.putIfAbsent(key, factory)

  override def run(testName: Option[String], args: Args): Status = {

    val existing = args.configMap.getWithDefault[ConfigMap]("PerRunResources", ConfigMap())
    val toCreateSerial = serialFactories.filter { pair => !existing.contains(pair._1) }
    ResourceFactory.usingAllSerial(toCreateSerial.values.toSeq:_*) { serialResources =>

      val toCreateParallel = parallelFactories.filter { pair => !existing.contains(pair._1) }
      ResourceFactory.usingAll(toCreateParallel.values.toSeq:_*) { resources =>
        val allKeys: Iterable[String] = serialFactories.keys ++ parallelFactories.keys
        val allResources = serialResources ++ resources
        val created = ConfigMap(allKeys.zip(allResources).toSeq: _*)
        val all = existing.++(created)
        val appendedConfigMap = args.configMap.+("PerRunResources" -> all)
        super.run(testName, args.copy(configMap = appendedConfigMap))
      }
    }
  }

  def getResourceFactory[R](configMap: ConfigMap, key: String) = {
    configMap.getRequired[ConfigMap]("PerRunResources").getRequired[ResourceFactory[R]](key)
  }
}
