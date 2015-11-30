package uk.org.lidalia
package exampleapp
package tests
package logging

import slf4jtest.{TestLogger, TestLoggerFactory}
import system.logging.LoggerFactory

object Slf4jTestLoggerFactory {

  def apply(
    factory: TestLoggerFactory = new TestLoggerFactory()
  ): Slf4jTestLoggerFactory = {
    new Slf4jTestLoggerFactory(factory)
  }
}

class Slf4jTestLoggerFactory private (
  factory: TestLoggerFactory
) extends LoggerFactory[TestLogger] {

  override def getLogger(name: String) = factory.getLogger(name)

  override def reset(): Unit = factory.clearAllLoggers()

}
