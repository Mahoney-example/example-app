package uk.org.lidalia
package exampleapp.system
package db.changelog

import liquibase.Liquibase
import liquibase.changelog.DatabaseChangeLog
import liquibase.database.Database
import liquibase.resource.ClassLoaderResourceAccessor

object Migrator {

  def apply(
    changelog: DatabaseChangeLog,
    database: Database
  ) = new Migrator(changelog, database)
}

class Migrator private (
  changelog: DatabaseChangeLog,
  database: Database
) extends Liquibase(
  "",
  new ClassLoaderResourceAccessor(),
  database
) {

  override def getDatabaseChangeLog = changelog

  def update() = update("migration")

}
