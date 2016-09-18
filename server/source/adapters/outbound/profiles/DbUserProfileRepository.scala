package uk.org.lidalia
package exampleapp.server.adapters.outbound.profiles

import exampleapp.system.db.Database
import exampleapp.server.domain.{User, UserId, UserProfileRepository}

object DbUserProfileRepository {

  def apply(database: Database) = new DbUserProfileRepository(database)
}

class DbUserProfileRepository private(
  database: Database
) extends UserProfileRepository {

  override def get(userId: UserId): ?[User] = ???

  override def update(user: User): Unit = ???

  override def create(user: User): Unit = ???
}
