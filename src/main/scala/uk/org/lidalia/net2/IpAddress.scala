package uk.org.lidalia.net2

object IpAddress {

  def apply(ipAddressStr: String): IpAddress = {
    IpV4Address.apply(ipAddressStr)
  }
}

abstract class IpAddress private[net2]() extends Host {

}
