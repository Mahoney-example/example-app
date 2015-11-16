import java.util

object ThreadTest {

  def main(args: Array[String]) {

    subCall()
  }

  def subCall(): Unit = {
    val thread = new ChildThread {
      override def run() = {
        Thread.sleep(3000L)
        val exception = new Exception
        exception.setStackTrace(getStackTrace)
        throw exception
      }
    }
    thread.start()
    Thread.sleep(1000L)
    println(thread.getStackTrace.toList)
  }
}


class ChildThread extends Thread {

  var parentStack: Option[Array[StackTraceElement]] = None

  override def start(): Unit = {
    val trace = Thread.currentThread().getStackTrace
    println("Original parentStack = "+trace.toList)
    parentStack = Some(util.Arrays.copyOfRange(trace, 2, trace.length))
    println("Set parentStack to "+parentStack.map(_.toList))
    super.start()
  }

  override def getStackTrace: Array[StackTraceElement] = {
    println("parentStack is "+parentStack.map(_.toList))
    parentStack match {
      case None => super.getStackTrace
      case Some(parentElements) => Array.concat(super.getStackTrace, parentElements)
    }
  }
}
