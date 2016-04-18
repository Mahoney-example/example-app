package uk.org.lidalia.exampleapp.server.web

import java.net.ServerSocket

import uk.org.lidalia.exampleapp.system.HasLogger
import uk.org.lidalia.exampleapp.server.application.Application
import uk.org.lidalia.scalalang.Reusable
import uk.org.lidalia.scalalang.TryFinally._try
import uk.org.lidalia.net.Port

case class Server(
  application: Application,
  config: ServerConfig
) extends Reusable with HasLogger {

  private var localPortVar: Option[Port] = None

  def localPort = localPortVar.get

  private [web] def start(): Unit = {
    localPortVar = Some(config.localPort.getOrElse(randomPort))
    log.info(s"Server started on port $localPort: $this")
  }

  private [web] def stop(): Unit = {
    log.info(s"Server stopped: $this")
  }

  private def randomPort: Port = {
    val socket = new ServerSocket(0)
    _try {
      Port(socket.getLocalPort)
    } _finally {
      socket.close()
    }
  }

  override def reset(): Unit = application.reset()

}
