package uk.org.lidalia
package exampleapp
package server.services.profiles

import system.db.Database

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
