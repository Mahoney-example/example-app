package uk.org.lidalia
package exampleapp.server.adapters.outbound

import scalalang.ResourceFactory

class AdaptersDefinition extends ResourceFactory[Adapters] {

  override def using[T](work: (Adapters) => T): T = ???

}
