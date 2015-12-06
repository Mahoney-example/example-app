import uk.org.lidalia.exampleapp.system.logging.LogbackLoggingDefinition
import uk.org.lidalia.scalalang.ChildThread

object ThreadTest {

  def main(args: Array[String]) {
    LogbackLoggingDefinition().using { () =>
      subCall()
    }
  }

  def subCall(): Unit = {
    val thread = new ChildThread("hi") {
      override def run() = {
        throw new Exception
      }
    }
    thread.start()
    thread.join()
  }
}



