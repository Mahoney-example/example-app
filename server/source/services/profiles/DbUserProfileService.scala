package uk.org.lidalia
package exampleapp.server.services.profiles

import javax.sql.DataSource

object DbUserProfileService {

  def apply(dataSource: DataSource) = new DbUserProfileService(dataSource)
}

class DbUserProfileService private (
  dataSource: DataSource
) extends UserProfileService {

  override def get(userId: UserId): ?[User] = ???

  override def update(user: User): Unit = ???

  override def create(user: User): Unit = ???
}
