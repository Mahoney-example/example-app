package uk.org.lidalia
package exampleapp.system
package logging

import org.slf4j.{Logger, ILoggerFactory}
import scalalang.{ResourceFactory, Reusable}

trait LoggerFactory[T <: Logger] extends ILoggerFactory with Reusable {
  def getLogger(name: String): T
  def getLogger(forClass: Class[_]): T = getLogger(forClass.getName)
}

object StaticLoggerFactory extends LoggerFactory[Logger] {

  val factory = org.slf4j.LoggerFactory.getILoggerFactory

  override def getLogger(s: String) = {
    factory.getLogger(s)
  }
}

object StaticLoggerFactoryDefinition extends ResourceFactory[LoggerFactory[Logger]] {

  override def using[T](work: (LoggerFactory[Logger]) => T) = work(StaticLoggerFactory)
}
