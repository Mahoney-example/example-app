package uk.org.lidalia
package exampleapp.system
package logging

import java.util.logging.Level.ALL
import java.util.logging.LogManager

import org.slf4j.bridge.SLF4JBridgeHandler

import scala.collection.JavaConversions.enumerationAsScalaIterator

object JuliConfigurer {

  def sendJuliToSlf4j() = {
    val manager = LogManager.getLogManager
    val loggers = manager.getLoggerNames.toIterable.map(manager.getLogger)
    loggers.foreach { logger =>
      logger.getHandlers.foreach(logger.removeHandler)
      logger.setLevel(ALL)
      logger.addHandler(new SLF4JBridgeHandler)
    }
  }
}
