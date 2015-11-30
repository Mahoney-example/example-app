import ch.qos.logback.classic.Level.INFO
import ch.qos.logback.classic.LoggerContext
import org.scalatest.{Outcome, Suites, fixture}
import org.slf4j.LoggerFactory.getILoggerFactory
import uk.org.lidalia.exampleapp.local.{Environment, EnvironmentDefinition}
import uk.org.lidalia.exampleapp.system.logging.LogbackLoggingDefinition
import uk.org.lidalia.scalalang.ResourceFactory

class RegisterTests(envDefinition: ResourceFactory[Environment] = EnvironmentDefinition()) extends fixture.FunSuite {

  override type FixtureParam = Environment

  test("hello world") { environment =>
    println("hello world"+environment.servers)
  }

  override protected def withFixture(test: OneArgTest): Outcome = {
    envDefinition.using { environment =>
      test.apply(environment)
    }
  }
}

object RegisterTests {

  def main(args: Array[String]) {

    val staticLoggerFactory = getILoggerFactory.asInstanceOf[LoggerContext]

    LogbackLoggingDefinition(
      staticLoggerFactory,
      "uk.org.lidalia" -> INFO
    ).using {loggerFactory =>
      val envDef = EnvironmentDefinition(loggerFactory = loggerFactory)
      new Suites(
        new RegisterTests(envDef),
        new RegisterTests(envDef),
        new RegisterTests(envDef),
        new RegisterTests(envDef)
      ).execute
    }
  }
}
