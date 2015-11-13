package uk.org.lidalia.exampleapp.server.services.profiles

import java.util.UUID

case class User(id: UserId)

case class UserId(id: UUID = UUID.randomUUID())
