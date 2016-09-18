package uk.org.lidalia
package exampleapp.server.domain

import java.util.UUID

case class User(id: UserId)

case class UserId(id: UUID = UUID.randomUUID())
