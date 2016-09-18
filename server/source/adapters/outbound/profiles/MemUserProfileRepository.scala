package uk.org.lidalia
package exampleapp.server.adapters.outbound.profiles

import scalalang.Lock
import uk.org.lidalia.exampleapp.server.domain.{User, UserId, UserProfileRepository}

import collection.mutable

object MemUserProfileRepository {

  def apply() = new MemUserProfileRepository()

}

class MemUserProfileRepository private() extends UserProfileRepository {

  private val lock = new Lock()
  private val profiles: mutable.Map[UserId, User] = mutable.Map()

  override def get(userId: UserId): ?[User] = {
    lock.readLock.using {
      profiles.get(userId)
    }
  }

  override def update(user: User): Unit = {
    lock.writeLock.using {
      if (profiles.contains(user.id)) {
        profiles(user.id) = user
      } else {
        throw new IllegalStateException(s"Cannot update $user; User ID ${user.id} does not yet exist")
      }
    }
  }

  override def create(user: User): Unit = {
    lock.writeLock.using {
      if (!profiles.contains(user.id)) {
        profiles(user.id) = user
      } else {
        throw new IllegalStateException(s"Cannot create $user; User ID ${user.id} already exists")
      }
    }
  }
}
