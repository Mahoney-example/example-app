import java.lang.Thread.UncaughtExceptionHandler
import java.util

import org.slf4j.{Logger, LoggerFactory}
import uk.org.lidalia.exampleapp.system.logging.LogbackConfigurer

object ThreadTest {

  LogbackConfigurer.configureLogback()

  def main(args: Array[String]) {

    subCall()
  }

  def subCall(): Unit = {
    val thread = new ChildThread {
      override def run() = {
        throw new Exception
      }
    }
    thread.start()
    thread.join()
  }
}


class ChildThread extends Thread {

  var parentStack: Option[Array[StackTraceElement]] = None

  setUncaughtExceptionHandler(new UncaughtExceptionHandler {
    override def uncaughtException(t: Thread, e: Throwable): Unit = {
      val fullStack = Array.concat(e.getStackTrace, t.asInstanceOf[ChildThread].parentStack.get)
      e.setStackTrace(fullStack)
      LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME).error("Uncaught exception in thread {}", t, e: Any)
    }
  })

  override def start(): Unit = {
    val trace = Thread.currentThread().getStackTrace
    parentStack = Some(util.Arrays.copyOfRange(trace, 2, trace.length))
    super.start()
  }

  override def getStackTrace: Array[StackTraceElement] = {
    parentStack match {
      case None => super.getStackTrace
      case Some(parentElements) => Array.concat(super.getStackTrace, parentElements)
    }
  }
}
