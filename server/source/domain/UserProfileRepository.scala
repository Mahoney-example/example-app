package uk.org.lidalia
package exampleapp.server.domain

trait UserProfileRepository {

  def get(userId: UserId): ?[User]
  def create(user: User): Unit
  def update(user: User): Unit

}
