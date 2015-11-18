package uk.org.lidalia
package exampleapp
package tests
package logging

import slf4jtest.{TestLogger, TestLoggerFactory}
import system.logging.LoggerFactory

class Slf4jTestLoggerFactory private (
  factory: TestLoggerFactory
) extends LoggerFactory[TestLogger] {

  override def getLogger(name: String) = factory.getLogger(name)

  override def reset(): Unit = factory.clearAllLoggers()

}
