package uk.org.lidalia
package exampleapp.server.adapters.outbound.profiles

import java.sql.Connection

import exampleapp.server.domain.{User, UserId, UserProfileRepository}
import uk.org.lidalia.scalalang.ResourceFactory

object DbUserProfileRepository {

  def apply(database: ResourceFactory[Connection]) = new DbUserProfileRepository(database)
}

class DbUserProfileRepository private(
  database: ResourceFactory[Connection]
) extends UserProfileRepository {

  override def get(userId: UserId): ?[User] = ???

  override def update(user: User): Unit = ???

  override def create(user: User): Unit = ???
}
