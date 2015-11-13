package uk.org.lidalia.exampleapp.system

import org.slf4j.LoggerFactory

trait HasLogger {

  protected lazy val log = LoggerFactory.getLogger(getClass.getName)
}
