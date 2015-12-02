import ch.qos.logback.classic.{Level, LoggerContext}
import org.scalatest.{Status, Args, Suites}
import org.slf4j.Logger
import uk.org.lidalia.exampleapp.system.logging.JulConfigurer.sendJulToSlf4j
import uk.org.lidalia.exampleapp.system.logging.StaticLoggerFactory.factory
import uk.org.lidalia.exampleapp.system.logging.{StaticLoggerFactory, LogbackLoggingDefinition, JulConfigurer}
import uk.org.lidalia.exampleapp.tests.functional.RegisterTests
import uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4J


class FunctionalTests extends Suites(
  new RegisterTests(),
  new RegisterTests(),
  new RegisterTests(),
  new RegisterTests(),
  new RegisterTests(),
  new RegisterTests(),
  new RegisterTests(),
  new RegisterTests(),
  new RegisterTests(),
  new RegisterTests(),
  new RegisterTests(),
  new RegisterTests(),
  new RegisterTests()
) {
  override def run(testName: Option[String], args: Args): Status = {

    sendJulToSlf4j()

    LogbackLoggingDefinition(
      factory.asInstanceOf[LoggerContext],
      Logger.ROOT_LOGGER_NAME -> Level.TRACE
    ).using { () =>

      super.run(testName, args)
    }
  }
}
