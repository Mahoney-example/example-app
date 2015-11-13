package uk.org.lidalia
package exampleapp.server.services.profiles

import scalalang.Lock

import collection.mutable

object MemUserProfileService {

  def apply() = new MemUserProfileService()

}

class MemUserProfileService private () extends UserProfileService {

  private val lock = new Lock()
  private val profiles: mutable.Map[UserId, User] = mutable.Map()

  override def get(userId: UserId): ?[User] = {
    lock.readLock.withA(() => profiles.get(userId))
  }

  override def update(user: User): Unit = {
    lock.writeLock.withA(() =>
      if (profiles.contains(user.id)) {
        profiles(user.id) = user
      } else {
        throw new IllegalStateException(s"Cannot update $user; User ID ${user.id} does not yet exist")
      }
    )
  }

  override def create(user: User): Unit = {
    lock.writeLock.withA(() =>
      if (!profiles.contains(user.id)) {
        profiles(user.id) = user
      } else {
        throw new IllegalStateException(s"Cannot create $user; User ID ${user.id} already exists")
      }
    )
  }
}
