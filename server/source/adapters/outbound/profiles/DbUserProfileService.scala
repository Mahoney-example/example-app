package uk.org.lidalia
package exampleapp.server.adapters.outbound.profiles

import exampleapp.system.db.Database
import exampleapp.server.domain.{User, UserId, UserProfileService}

object DbUserProfileService {

  def apply(database: Database) = new DbUserProfileService(database)
}

class DbUserProfileService private (
  database: Database
) extends UserProfileService {

  override def get(userId: UserId): ?[User] = ???

  override def update(user: User): Unit = ???

  override def create(user: User): Unit = ???
}
