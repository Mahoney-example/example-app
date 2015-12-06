package db.changelog

import liquibase.changelog.{ChangeLogHistoryService, StandardChangeLogHistoryService}
import liquibase.database.Database
import liquibase.database.core.{HsqlDatabase, PostgresDatabase}
import liquibase.datatype.LiquibaseDataType
import liquibase.datatype.core.{BigIntType, BlobType, BooleanType, CharType, ClobType, CurrencyType, DatabaseFunctionType, DateTimeType, DateType, DecimalType, DoubleType, FloatType, IntType, MediumIntType, NCharType, NVarcharType, NumberType, SmallIntType, TimeType, TimestampType, TinyIntType, UUIDType, UnknownType, VarcharType}
import liquibase.diff.DiffGenerator
import liquibase.diff.compare.DatabaseObjectComparator
import liquibase.diff.compare.core.{CatalogComparator, ColumnComparator, DefaultDatabaseObjectComparator, ForeignKeyComparator, IndexComparator, PrimaryKeyComparator, SchemaComparator, TableComparator, UniqueConstraintComparator}
import liquibase.diff.core.StandardDiffGenerator
import liquibase.executor.Executor
import liquibase.executor.jvm.JdbcExecutor
import liquibase.ext.logging.slf4j.Slf4jLogger
import liquibase.lockservice.{LockService, StandardLockService}
import liquibase.logging.Logger
import liquibase.sdk.resource.MockResourceAccessor
import liquibase.servicelocator.{DefaultPackageScanClassResolver, ServiceLocator}
import liquibase.snapshot.SnapshotGenerator
import liquibase.snapshot.jvm.{CatalogSnapshotGenerator, ColumnSnapshotGenerator, DataSnapshotGenerator, ForeignKeySnapshotGenerator, IndexSnapshotGenerator, PrimaryKeySnapshotGenerator, SchemaSnapshotGenerator, SequenceSnapshotGenerator, TableSnapshotGenerator, UniqueConstraintSnapshotGenerator, ViewSnapshotGenerator}
import liquibase.sqlgenerator.SqlGenerator
import liquibase.sqlgenerator.core._
import liquibase.statement.UniqueConstraint
import liquibase.structure.DatabaseObject
import liquibase.structure.core.{Catalog, Column, Data, ForeignKey, Index, PrimaryKey, Schema, Sequence, StoredProcedure, Table, View}

object FastLiquibase {

  def apply() = ServiceLocator.setInstance(new FastServiceLocator)

}
class FastServiceLocator extends ServiceLocator(new DefaultPackageScanClassResolver, new MockResourceAccessor) {

  private val typeToClass: Map[Class[_], Array[Class[_]]] = Map(
    classOf[DatabaseObject] -> Array(
      classOf[Data],
      classOf[PrimaryKey],
      classOf[Schema],
      classOf[Column],
      classOf[UniqueConstraint],
      classOf[Index],
      classOf[View],
      classOf[Table],
      classOf[ForeignKey],
      classOf[StoredProcedure],
      classOf[Catalog],
      classOf[Sequence]
    ),
    classOf[LiquibaseDataType] -> Array(
      classOf[DateType],
      classOf[IntType],
      classOf[ClobType],
      classOf[BlobType],
      classOf[UnknownType],
      classOf[MediumIntType],
      classOf[DoubleType],
      classOf[CurrencyType],
      classOf[BigIntType],
      classOf[TinyIntType],
      classOf[BooleanType],
      classOf[CharType],
      classOf[VarcharType],
      classOf[NCharType],
      classOf[NVarcharType],
      classOf[DateTimeType],
      classOf[TimestampType],
      classOf[TimeType],
      classOf[UUIDType],
      classOf[FloatType],
      classOf[SmallIntType],
      classOf[NumberType],
      classOf[DecimalType],
      classOf[DatabaseFunctionType]
    ),
    classOf[SqlGenerator[_]] -> Array(
      classOf[SelectFromDatabaseChangeLogLockGenerator],
      classOf[RenameColumnGenerator],
      classOf[StoredProcedureGenerator],
      classOf[MarkChangeSetRanGenerator],
      classOf[SetTableRemarksGenerator],
      classOf[SetColumnRemarksGenerator],
      classOf[CreateTableGenerator],
      classOf[CreateTableGeneratorInformix],
      //  sqlGeneratorFactory.register(new ReorganizeTableGeneratorDB2)
      classOf[AddAutoIncrementGenerator],
      //  classOf[AddAutoIncrementGeneratorMySQL],
      //  classOf[AddAutoIncrementGeneratorSQLite],
      //  classOf[AddAutoIncrementGeneratorInformix],
      classOf[AddAutoIncrementGeneratorHsqlH2],
      //  sqlGeneratorFactory.register(new AddAutoIncrementGeneratorDB2)
      //  sqlGeneratorFactory.register(new FindForeignKeyConstraintsGeneratorDB2)
      //  classOf[FindForeignKeyConstraintsGeneratorFirebird],
      classOf[FindForeignKeyConstraintsGeneratorHsql],
      //  classOf[FindForeignKeyConstraintsGeneratorOracle],
      //  classOf[FindForeignKeyConstraintsGeneratorMySQL],
      //  classOf[FindForeignKeyConstraintsGeneratorMSSQL],
      classOf[FindForeignKeyConstraintsGeneratorPostgres],
      classOf[UpdateDataChangeGenerator],
      classOf[DropColumnGenerator],
      classOf[DropIndexGenerator],
      classOf[TableRowCountGenerator],
      classOf[AddColumnGenerator],
      //  classOf[AddColumnGeneratorSQLite],
      classOf[AddColumnGeneratorDefaultClauseBeforeNotNull],
      classOf[CopyRowsGenerator],
      classOf[AddForeignKeyConstraintGenerator],
      classOf[InsertGenerator],
      classOf[UpdateGenerator],
      classOf[RenameViewGenerator],
      classOf[CreateDatabaseChangeLogTableGenerator],
      //  classOf[CreateDatabaseChangeLogTableGeneratorFirebird],
      classOf[DropForeignKeyConstraintGenerator],
      classOf[AddUniqueConstraintGenerator],
      classOf[DropTableGenerator],
      classOf[DropSequenceGenerator],
      classOf[UpdateChangeSetChecksumGenerator],
      classOf[CreateViewGenerator],
      classOf[LockDatabaseChangeLogGenerator],
      classOf[InsertDataChangeGenerator],
      classOf[RenameSequenceGenerator],
      //  sqlGeneratorFactory.register(new InsertOrUpdateGeneratorDB2)
      //  classOf[InsertOrUpdateGeneratorOracle],
      classOf[InsertOrUpdateGeneratorPostgres],
      classOf[InsertOrUpdateGeneratorHsql],
      //  classOf[InsertOrUpdateGeneratorMSSQL],
      //  classOf[InsertOrUpdateGeneratorMySQL],
      //  sqlGeneratorFactory.register(new InsertOrUpdateGeneratorH2)
      classOf[DropUniqueConstraintGenerator],
      classOf[AlterSequenceGenerator],
      classOf[DropViewGenerator],
      classOf[DropPrimaryKeyGenerator],
      classOf[ClearDatabaseChangeLogTableGenerator],
      classOf[DropDefaultValueGenerator],
      //  classOf[CreateViewGeneratorInformix],
      classOf[AddDefaultValueGenerator],
      //  classOf[AddDefaultValueGeneratorMySQL],
      //  classOf[AddDefaultValueGeneratorInformix],
      classOf[AddDefaultValueGeneratorPostgres],
      //  classOf[AddDefaultValueGeneratorSybase],
      //  classOf[AddDefaultValueGeneratorDerby],
      //  classOf[AddDefaultValueGeneratorOracle],
      //  classOf[AddDefaultValueGeneratorMSSQL],
      //  classOf[AddDefaultValueGeneratorSybaseASA],
      //  classOf[AddDefaultValueSQLite],
      classOf[CreateDatabaseChangeLogLockTableGenerator],
      classOf[DropProcedureGenerator],
      classOf[AddPrimaryKeyGenerator],
      //  classOf[AddPrimaryKeyGeneratorInformix],
      classOf[RenameTableGenerator],
      classOf[SetNullableGenerator],
      classOf[CreateProcedureGenerator],
      classOf[CreateIndexGenerator],
      classOf[CreateIndexGeneratorPostgres],
      classOf[CommentGenerator],
      classOf[GetViewDefinitionGenerator],
      //  sqlGeneratorFactory.register(new GetViewDefinitionGeneratorDB2)
      //  classOf[GetViewDefinitionGeneratorDerby],
      //  classOf[GetViewDefinitionGeneratorFirebird],
      classOf[GetViewDefinitionGeneratorHsql],
      //  classOf[GetViewDefinitionGeneratorInformix],
      //  classOf[GetViewDefinitionGeneratorMSSQL],
      //  classOf[GetViewDefinitionGeneratorOracle],
      classOf[GetViewDefinitionGeneratorPostgres],
      //  classOf[GetViewDefinitionGeneratorSybase],
      //  classOf[GetViewDefinitionGeneratorSybaseASA],
      classOf[ModifyDataTypeGenerator],
      classOf[RemoveChangeSetRanStatusGenerator],
      classOf[RuntimeGenerator],
      classOf[RawSqlGenerator],
      //  classOf[ReindexGeneratorSQLite],
      classOf[UnlockDatabaseChangeLogGenerator],
      classOf[InitializeDatabaseChangeLogLockTableGenerator],
      classOf[TagDatabaseGenerator],
      //  classOf[CreateDatabaseChangeLogTableGeneratorSybase],
      classOf[DeleteGenerator],
      classOf[CreateSequenceGenerator],
      classOf[GetNextChangeSetSequenceValueGenerator],
      classOf[SelectFromDatabaseChangeLogGenerator]
    ),
    classOf[DatabaseObjectComparator] -> Array(
      classOf[CatalogComparator],
      classOf[TableComparator],
      classOf[ForeignKeyComparator],
      classOf[DefaultDatabaseObjectComparator],
      classOf[ColumnComparator],
      classOf[UniqueConstraintComparator],
      classOf[IndexComparator],
      classOf[PrimaryKeyComparator],
      classOf[SchemaComparator]
    ),
    classOf[SnapshotGenerator] -> Array(
      classOf[DataSnapshotGenerator],
      classOf[ColumnSnapshotGenerator],
      //  classOf[H2ColumnSnapshotGenerator],
      classOf[TableSnapshotGenerator],
      classOf[ForeignKeySnapshotGenerator],
      classOf[PrimaryKeySnapshotGenerator],
      classOf[ViewSnapshotGenerator],
      classOf[CatalogSnapshotGenerator],
      classOf[IndexSnapshotGenerator],
      classOf[SequenceSnapshotGenerator],
      classOf[UniqueConstraintSnapshotGenerator],
      classOf[SchemaSnapshotGenerator]
    ),
    classOf[Database] -> Array(classOf[PostgresDatabase], classOf[HsqlDatabase]),
    classOf[LockService] -> Array(classOf[StandardLockService]),
    classOf[ChangeLogHistoryService] -> Array(classOf[StandardChangeLogHistoryService]),
    classOf[DiffGenerator] -> Array(classOf[StandardDiffGenerator])
  )

  override def newInstance(requiredInterface: Class[_]) = {
    if (requiredInterface eq classOf[Logger]) new Slf4jLogger
    else if (requiredInterface eq classOf[Executor]) new JdbcExecutor
    else throw new IllegalArgumentException("cannot find a " + requiredInterface)
  }

  override def findClasses[T](requiredInterface: Class[T]): Array[Class[_ <: T]] = {
    typeToClass.getOrElse(requiredInterface, Array()).asInstanceOf[Array[Class[_ <: T]]]
  }
}
