package uk.org.lidalia.exampleapp

import java.util.concurrent.CountDownLatch

package object system {

  val blockUntilShutdown = () => {

    val latch = new CountDownLatch(1)
    val runningThread = Thread.currentThread()

    Runtime.getRuntime.addShutdownHook(
      new Thread {
        override def run(): Unit = {
          latch.countDown()
          runningThread.join()
        }
      }
    )

    latch.await()
  }
}
