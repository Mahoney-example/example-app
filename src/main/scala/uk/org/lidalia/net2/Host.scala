package uk.org.lidalia.net2

object Host {

  def apply(hostStr: String): Host = HostParser.parse(hostStr)

}

abstract class Host private[net2]() extends Immutable

object HostParser {
  def parse(hostStr: String): Host = {
    ???
  }
}
