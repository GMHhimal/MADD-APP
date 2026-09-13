package com.lumina.app.`data`.local

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.lumina.app.`data`.model.Priority
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class DeadlineDao_Impl(
  __db: RoomDatabase,
) : DeadlineDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfDeadlineEntity: EntityInsertAdapter<DeadlineEntity>

  private val __luminaConverters: LuminaConverters = LuminaConverters()

  private val __deleteAdapterOfDeadlineEntity: EntityDeleteOrUpdateAdapter<DeadlineEntity>

  private val __updateAdapterOfDeadlineEntity: EntityDeleteOrUpdateAdapter<DeadlineEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfDeadlineEntity = object : EntityInsertAdapter<DeadlineEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR ABORT INTO `deadlines` (`id`,`title`,`course`,`dueAt`,`progress`,`priority`) VALUES (nullif(?, 0),?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: DeadlineEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.title)
        statement.bindText(3, entity.course)
        statement.bindLong(4, entity.dueAt)
        statement.bindLong(5, entity.progress.toLong())
        val _tmp: String = __luminaConverters.priorityToString(entity.priority)
        statement.bindText(6, _tmp)
      }
    }
    this.__deleteAdapterOfDeadlineEntity = object : EntityDeleteOrUpdateAdapter<DeadlineEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `deadlines` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: DeadlineEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfDeadlineEntity = object : EntityDeleteOrUpdateAdapter<DeadlineEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `deadlines` SET `id` = ?,`title` = ?,`course` = ?,`dueAt` = ?,`progress` = ?,`priority` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: DeadlineEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.title)
        statement.bindText(3, entity.course)
        statement.bindLong(4, entity.dueAt)
        statement.bindLong(5, entity.progress.toLong())
        val _tmp: String = __luminaConverters.priorityToString(entity.priority)
        statement.bindText(6, _tmp)
        statement.bindLong(7, entity.id)
      }
    }
  }

  public override suspend fun insert(deadline: DeadlineEntity): Long = performSuspending(__db,
      false, true) { _connection ->
    val _result: Long = __insertAdapterOfDeadlineEntity.insertAndReturnId(_connection, deadline)
    _result
  }

  public override suspend fun insertAll(items: List<DeadlineEntity>): Unit = performSuspending(__db,
      false, true) { _connection ->
    __insertAdapterOfDeadlineEntity.insert(_connection, items)
  }

  public override suspend fun delete(deadline: DeadlineEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __deleteAdapterOfDeadlineEntity.handle(_connection, deadline)
  }

  public override suspend fun update(deadline: DeadlineEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __updateAdapterOfDeadlineEntity.handle(_connection, deadline)
  }

  public override fun observeAll(): Flow<List<DeadlineEntity>> {
    val _sql: String = "SELECT * FROM deadlines ORDER BY dueAt ASC"
    return createFlow(__db, false, arrayOf("deadlines")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfCourse: Int = getColumnIndexOrThrow(_stmt, "course")
        val _columnIndexOfDueAt: Int = getColumnIndexOrThrow(_stmt, "dueAt")
        val _columnIndexOfProgress: Int = getColumnIndexOrThrow(_stmt, "progress")
        val _columnIndexOfPriority: Int = getColumnIndexOrThrow(_stmt, "priority")
        val _result: MutableList<DeadlineEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: DeadlineEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpCourse: String
          _tmpCourse = _stmt.getText(_columnIndexOfCourse)
          val _tmpDueAt: Long
          _tmpDueAt = _stmt.getLong(_columnIndexOfDueAt)
          val _tmpProgress: Int
          _tmpProgress = _stmt.getLong(_columnIndexOfProgress).toInt()
          val _tmpPriority: Priority
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfPriority)
          _tmpPriority = __luminaConverters.stringToPriority(_tmp)
          _item = DeadlineEntity(_tmpId,_tmpTitle,_tmpCourse,_tmpDueAt,_tmpProgress,_tmpPriority)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun count(): Int {
    val _sql: String = "SELECT COUNT(*) FROM deadlines"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: Int
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun byId(id: Long): DeadlineEntity? {
    val _sql: String = "SELECT * FROM deadlines WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfCourse: Int = getColumnIndexOrThrow(_stmt, "course")
        val _columnIndexOfDueAt: Int = getColumnIndexOrThrow(_stmt, "dueAt")
        val _columnIndexOfProgress: Int = getColumnIndexOrThrow(_stmt, "progress")
        val _columnIndexOfPriority: Int = getColumnIndexOrThrow(_stmt, "priority")
        val _result: DeadlineEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpCourse: String
          _tmpCourse = _stmt.getText(_columnIndexOfCourse)
          val _tmpDueAt: Long
          _tmpDueAt = _stmt.getLong(_columnIndexOfDueAt)
          val _tmpProgress: Int
          _tmpProgress = _stmt.getLong(_columnIndexOfProgress).toInt()
          val _tmpPriority: Priority
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfPriority)
          _tmpPriority = __luminaConverters.stringToPriority(_tmp)
          _result = DeadlineEntity(_tmpId,_tmpTitle,_tmpCourse,_tmpDueAt,_tmpProgress,_tmpPriority)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
