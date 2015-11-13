package uk.org.lidalia.exampleapp.server.services

import uk.org.lidalia.scalalang.ResourceFactory

class ServicesDefinition extends ResourceFactory[Services] {

  override def using[T](work: (Services) => T): T = ???

}
