package uk.org.lidalia
package exampleapp.system
package db.changelog

import liquibase.Liquibase
import liquibase.change.{ColumnConfig, Change}
import liquibase.change.core.{AddPrimaryKeyChange, CreateTableChange}
import liquibase.changelog.{ChangeSet, DatabaseChangeLog}
import liquibase.database.ObjectQuotingStrategy.QUOTE_ONLY_RESERVED_WORDS
import liquibase.database.Database
import liquibase.resource.ClassLoaderResourceAccessor

object Migrator {

  def apply(
    changelog: DatabaseChangeLog,
    database: Database
  ) = new Migrator(changelog, database)

  def changeLog(changeSets: ((DatabaseChangeLog) => ChangeSet)*) = {
    val result = new DatabaseChangeLog()
    result.setObjectQuotingStrategy(QUOTE_ONLY_RESERVED_WORDS)
    changeSets.foreach { changeSetBuilder =>
      result.addChangeSet(changeSetBuilder(result))
    }
    result
  }

  def changeSet(
    id: String,

    changes: Change*
  ): (DatabaseChangeLog) => ChangeSet = {
    (changeLog) => {
      val result = new ChangeSet(
        id,
        "system",
        false,
        false,
        "",
        null,
        null,
        true,
        QUOTE_ONLY_RESERVED_WORDS,
        changeLog
      )
      changes.foreach(result.addChange)
      result
    }
  }

  def createTable(tableName: String, columns: ColumnConfig*) = {
    val result = new CreateTableChange()
    result.setTableName(tableName)
    columns.foreach(result.addColumn)
    result
  }

  def column(name: String, dataType: String) = {
    val result = new ColumnConfig()
    result.setName(name)
    result.setType(dataType)
    result
  }

  def addPrimaryKey(
    constraintName: String,
    tableName: String,
    columnNames: String*
  ) = {
    val result = new AddPrimaryKeyChange
    result.setConstraintName(constraintName)
    result.setTableName(tableName)
    result.setColumnNames(columnNames.mkString(", "))
    result
  }
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

  def update(): Unit = update("migration")

}
