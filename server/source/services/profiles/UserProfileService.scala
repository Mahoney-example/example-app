package uk.org.lidalia
package exampleapp.server.services.profiles

trait UserProfileService {

  def get(userId: UserId): ?[User]
  def create(user: User): Unit
  def update(user: User): Unit

}
