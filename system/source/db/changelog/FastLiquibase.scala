package db.changelog

import liquibase.changelog.{ChangeLogHistoryServiceFactory, StandardChangeLogHistoryService}
import liquibase.database.DatabaseFactory
import liquibase.database.core.{HsqlDatabase, PostgresDatabase}
import liquibase.datatype.DataTypeFactory
import liquibase.datatype.core.{BigIntType, BlobType, BooleanType, CharType, ClobType, CurrencyType, DatabaseFunctionType, DateTimeType, DateType, DecimalType, DoubleType, FloatType, IntType, MediumIntType, NCharType, NVarcharType, NumberType, SmallIntType, TimeType, TimestampType, TinyIntType, UUIDType, UnknownType, VarcharType}
import liquibase.diff.DiffGeneratorFactory
import liquibase.diff.compare.DatabaseObjectComparatorFactory
import liquibase.diff.compare.core.{CatalogComparator, ColumnComparator, DefaultDatabaseObjectComparator, ForeignKeyComparator, IndexComparator, PrimaryKeyComparator, SchemaComparator, TableComparator, UniqueConstraintComparator}
import liquibase.diff.core.StandardDiffGenerator
import liquibase.executor.Executor
import liquibase.executor.jvm.JdbcExecutor
import liquibase.ext.logging.slf4j.Slf4jLogger
import liquibase.lockservice.{LockServiceFactory, StandardLockService}
import liquibase.logging.Logger
import liquibase.servicelocator.ServiceLocator
import liquibase.snapshot.SnapshotGeneratorFactory
import liquibase.snapshot.jvm.{CatalogSnapshotGenerator, ColumnSnapshotGenerator, DataSnapshotGenerator, ForeignKeySnapshotGenerator, H2ColumnSnapshotGenerator, IndexSnapshotGenerator, PrimaryKeySnapshotGenerator, SchemaSnapshotGenerator, SequenceSnapshotGenerator, TableSnapshotGenerator, UniqueConstraintSnapshotGenerator, ViewSnapshotGenerator}
import liquibase.sqlgenerator.SqlGeneratorFactory
import liquibase.sqlgenerator.core._
import liquibase.statement.UniqueConstraint
import liquibase.structure.DatabaseObject
import liquibase.structure.core.{Catalog, Column, Data, ForeignKey, Index, PrimaryKey, Schema, Sequence, StoredProcedure, Table, View}

object FastLiquibase {

  ServiceLocator.setInstance(new FastServiceLocator)

  DatabaseFactory.getInstance().register(new PostgresDatabase)
  DatabaseFactory.getInstance().register(new HsqlDatabase)

  LockServiceFactory.getInstance().register(new StandardLockService)
  ChangeLogHistoryServiceFactory.getInstance().register(new StandardChangeLogHistoryService)
  DiffGeneratorFactory.getInstance().register(new StandardDiffGenerator)

  private val snapshotGeneratorFactory = SnapshotGeneratorFactory.getInstance()
  snapshotGeneratorFactory.register(new DataSnapshotGenerator)
  snapshotGeneratorFactory.register(new ColumnSnapshotGenerator)
//  snapshotGeneratorFactory.register(new H2ColumnSnapshotGenerator)
  snapshotGeneratorFactory.register(new TableSnapshotGenerator)
  snapshotGeneratorFactory.register(new ForeignKeySnapshotGenerator)
  snapshotGeneratorFactory.register(new PrimaryKeySnapshotGenerator)
  snapshotGeneratorFactory.register(new ViewSnapshotGenerator)
  snapshotGeneratorFactory.register(new CatalogSnapshotGenerator)
  snapshotGeneratorFactory.register(new IndexSnapshotGenerator)
  snapshotGeneratorFactory.register(new SequenceSnapshotGenerator)
  snapshotGeneratorFactory.register(new UniqueConstraintSnapshotGenerator)
  snapshotGeneratorFactory.register(new SchemaSnapshotGenerator)

  private val databaseObjectComparatorFactory = DatabaseObjectComparatorFactory.getInstance()
  databaseObjectComparatorFactory.register(new CatalogComparator)
  databaseObjectComparatorFactory.register(new TableComparator)
  databaseObjectComparatorFactory.register(new ForeignKeyComparator)
  databaseObjectComparatorFactory.register(new DefaultDatabaseObjectComparator)
  databaseObjectComparatorFactory.register(new ColumnComparator)
  databaseObjectComparatorFactory.register(new UniqueConstraintComparator)
  databaseObjectComparatorFactory.register(new IndexComparator)
  databaseObjectComparatorFactory.register(new PrimaryKeyComparator)
  databaseObjectComparatorFactory.register(new SchemaComparator)

  SqlGeneratorFactory.getInstance().register(new SelectFromDatabaseChangeLogLockGenerator)

  private val sqlGeneratorFactory = SqlGeneratorFactory.getInstance()
  sqlGeneratorFactory.register(new SelectFromDatabaseChangeLogLockGenerator)
  sqlGeneratorFactory.register(new RenameColumnGenerator)
  sqlGeneratorFactory.register(new StoredProcedureGenerator)
  sqlGeneratorFactory.register(new MarkChangeSetRanGenerator)
  sqlGeneratorFactory.register(new SetTableRemarksGenerator)
  sqlGeneratorFactory.register(new SetColumnRemarksGenerator)
  sqlGeneratorFactory.register(new CreateTableGenerator)
  sqlGeneratorFactory.register(new CreateTableGeneratorInformix)
//  sqlGeneratorFactory.register(new ReorganizeTableGeneratorDB2)
  sqlGeneratorFactory.register(new AddAutoIncrementGenerator)
//  sqlGeneratorFactory.register(new AddAutoIncrementGeneratorMySQL)
//  sqlGeneratorFactory.register(new AddAutoIncrementGeneratorSQLite)
//  sqlGeneratorFactory.register(new AddAutoIncrementGeneratorInformix)
  sqlGeneratorFactory.register(new AddAutoIncrementGeneratorHsqlH2)
//  sqlGeneratorFactory.register(new AddAutoIncrementGeneratorDB2)
//  sqlGeneratorFactory.register(new FindForeignKeyConstraintsGeneratorDB2)
//  sqlGeneratorFactory.register(new FindForeignKeyConstraintsGeneratorFirebird)
  sqlGeneratorFactory.register(new FindForeignKeyConstraintsGeneratorHsql)
//  sqlGeneratorFactory.register(new FindForeignKeyConstraintsGeneratorOracle)
//  sqlGeneratorFactory.register(new FindForeignKeyConstraintsGeneratorMySQL)
//  sqlGeneratorFactory.register(new FindForeignKeyConstraintsGeneratorMSSQL)
  sqlGeneratorFactory.register(new FindForeignKeyConstraintsGeneratorPostgres)
  sqlGeneratorFactory.register(new UpdateDataChangeGenerator)
  sqlGeneratorFactory.register(new DropColumnGenerator)
  sqlGeneratorFactory.register(new DropIndexGenerator)
  sqlGeneratorFactory.register(new TableRowCountGenerator)
  sqlGeneratorFactory.register(new AddColumnGenerator)
//  sqlGeneratorFactory.register(new AddColumnGeneratorSQLite)
  sqlGeneratorFactory.register(new AddColumnGeneratorDefaultClauseBeforeNotNull)
  sqlGeneratorFactory.register(new CopyRowsGenerator)
  sqlGeneratorFactory.register(new AddForeignKeyConstraintGenerator)
  sqlGeneratorFactory.register(new InsertGenerator)
  sqlGeneratorFactory.register(new UpdateGenerator)
  sqlGeneratorFactory.register(new RenameViewGenerator)
  sqlGeneratorFactory.register(new CreateDatabaseChangeLogTableGenerator)
//  sqlGeneratorFactory.register(new CreateDatabaseChangeLogTableGeneratorFirebird)
  sqlGeneratorFactory.register(new DropForeignKeyConstraintGenerator)
  sqlGeneratorFactory.register(new AddUniqueConstraintGenerator)
  sqlGeneratorFactory.register(new DropTableGenerator)
  sqlGeneratorFactory.register(new DropSequenceGenerator)
  sqlGeneratorFactory.register(new UpdateChangeSetChecksumGenerator)
  sqlGeneratorFactory.register(new CreateViewGenerator)
  sqlGeneratorFactory.register(new LockDatabaseChangeLogGenerator)
  sqlGeneratorFactory.register(new InsertDataChangeGenerator)
  sqlGeneratorFactory.register(new RenameSequenceGenerator)
//  sqlGeneratorFactory.register(new InsertOrUpdateGeneratorDB2)
//  sqlGeneratorFactory.register(new InsertOrUpdateGeneratorOracle)
  sqlGeneratorFactory.register(new InsertOrUpdateGeneratorPostgres)
  sqlGeneratorFactory.register(new InsertOrUpdateGeneratorHsql)
//  sqlGeneratorFactory.register(new InsertOrUpdateGeneratorMSSQL)
//  sqlGeneratorFactory.register(new InsertOrUpdateGeneratorMySQL)
//  sqlGeneratorFactory.register(new InsertOrUpdateGeneratorH2)
  sqlGeneratorFactory.register(new DropUniqueConstraintGenerator)
  sqlGeneratorFactory.register(new AlterSequenceGenerator)
  sqlGeneratorFactory.register(new DropViewGenerator)
  sqlGeneratorFactory.register(new DropPrimaryKeyGenerator)
  sqlGeneratorFactory.register(new ClearDatabaseChangeLogTableGenerator)
  sqlGeneratorFactory.register(new DropDefaultValueGenerator)
//  sqlGeneratorFactory.register(new CreateViewGeneratorInformix)
  sqlGeneratorFactory.register(new AddDefaultValueGenerator)
//  sqlGeneratorFactory.register(new AddDefaultValueGeneratorMySQL)
//  sqlGeneratorFactory.register(new AddDefaultValueGeneratorInformix)
  sqlGeneratorFactory.register(new AddDefaultValueGeneratorPostgres)
//  sqlGeneratorFactory.register(new AddDefaultValueGeneratorSybase)
//  sqlGeneratorFactory.register(new AddDefaultValueGeneratorDerby)
//  sqlGeneratorFactory.register(new AddDefaultValueGeneratorOracle)
//  sqlGeneratorFactory.register(new AddDefaultValueGeneratorMSSQL)
//  sqlGeneratorFactory.register(new AddDefaultValueGeneratorSybaseASA)
//  sqlGeneratorFactory.register(new AddDefaultValueSQLite)
  sqlGeneratorFactory.register(new CreateDatabaseChangeLogLockTableGenerator)
  sqlGeneratorFactory.register(new DropProcedureGenerator)
  sqlGeneratorFactory.register(new AddPrimaryKeyGenerator)
//  sqlGeneratorFactory.register(new AddPrimaryKeyGeneratorInformix)
  sqlGeneratorFactory.register(new RenameTableGenerator)
  sqlGeneratorFactory.register(new SetNullableGenerator)
  sqlGeneratorFactory.register(new CreateProcedureGenerator)
  sqlGeneratorFactory.register(new CreateIndexGenerator)
  sqlGeneratorFactory.register(new CreateIndexGeneratorPostgres)
  sqlGeneratorFactory.register(new CommentGenerator)
  sqlGeneratorFactory.register(new GetViewDefinitionGenerator)
//  sqlGeneratorFactory.register(new GetViewDefinitionGeneratorDB2)
//  sqlGeneratorFactory.register(new GetViewDefinitionGeneratorDerby)
//  sqlGeneratorFactory.register(new GetViewDefinitionGeneratorFirebird)
  sqlGeneratorFactory.register(new GetViewDefinitionGeneratorHsql)
//  sqlGeneratorFactory.register(new GetViewDefinitionGeneratorInformix)
//  sqlGeneratorFactory.register(new GetViewDefinitionGeneratorMSSQL)
//  sqlGeneratorFactory.register(new GetViewDefinitionGeneratorOracle)
  sqlGeneratorFactory.register(new GetViewDefinitionGeneratorPostgres)
//  sqlGeneratorFactory.register(new GetViewDefinitionGeneratorSybase)
//  sqlGeneratorFactory.register(new GetViewDefinitionGeneratorSybaseASA)
  sqlGeneratorFactory.register(new ModifyDataTypeGenerator)
  sqlGeneratorFactory.register(new RemoveChangeSetRanStatusGenerator)
  sqlGeneratorFactory.register(new RuntimeGenerator)
  sqlGeneratorFactory.register(new RawSqlGenerator)
//  sqlGeneratorFactory.register(new ReindexGeneratorSQLite)
  sqlGeneratorFactory.register(new UnlockDatabaseChangeLogGenerator)
  sqlGeneratorFactory.register(new InitializeDatabaseChangeLogLockTableGenerator)
  sqlGeneratorFactory.register(new TagDatabaseGenerator)
//  sqlGeneratorFactory.register(new CreateDatabaseChangeLogTableGeneratorSybase)
  sqlGeneratorFactory.register(new DeleteGenerator)
  sqlGeneratorFactory.register(new CreateSequenceGenerator)
  sqlGeneratorFactory.register(new GetNextChangeSetSequenceValueGenerator)
  sqlGeneratorFactory.register(new SelectFromDatabaseChangeLogGenerator)

  private val dataTypeFactory = DataTypeFactory.getInstance()
  dataTypeFactory.register(classOf[DateType])
  dataTypeFactory.register(classOf[IntType])
  dataTypeFactory.register(classOf[ClobType])
  dataTypeFactory.register(classOf[BlobType])
  dataTypeFactory.register(classOf[UnknownType])
  dataTypeFactory.register(classOf[MediumIntType])
  dataTypeFactory.register(classOf[DoubleType])
  dataTypeFactory.register(classOf[CurrencyType])
  dataTypeFactory.register(classOf[BigIntType])
  dataTypeFactory.register(classOf[TinyIntType])
  dataTypeFactory.register(classOf[BooleanType])
  dataTypeFactory.register(classOf[CharType])
  dataTypeFactory.register(classOf[VarcharType])
  dataTypeFactory.register(classOf[NCharType])
  dataTypeFactory.register(classOf[NVarcharType])
  dataTypeFactory.register(classOf[DateTimeType])
  dataTypeFactory.register(classOf[TimestampType])
  dataTypeFactory.register(classOf[TimeType])
  dataTypeFactory.register(classOf[UUIDType])
  dataTypeFactory.register(classOf[FloatType])
  dataTypeFactory.register(classOf[SmallIntType])
  dataTypeFactory.register(classOf[NumberType])
  dataTypeFactory.register(classOf[DecimalType])
  dataTypeFactory.register(classOf[DatabaseFunctionType])
}
class FastServiceLocator extends ServiceLocator {

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
    )
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
