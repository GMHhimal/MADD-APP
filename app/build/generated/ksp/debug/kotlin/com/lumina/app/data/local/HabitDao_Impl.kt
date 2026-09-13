package com.lumina.app.`data`.local

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Boolean
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
public class HabitDao_Impl(
  __db: RoomDatabase,
) : HabitDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfHabitEntity: EntityInsertAdapter<HabitEntity>

  private val __deleteAdapterOfHabitEntity: EntityDeleteOrUpdateAdapter<HabitEntity>

  private val __updateAdapterOfHabitEntity: EntityDeleteOrUpdateAdapter<HabitEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfHabitEntity = object : EntityInsertAdapter<HabitEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR ABORT INTO `habits` (`id`,`title`,`iconKey`,`detail`,`scheduledMinutes`,`targetCount`,`currentCount`,`isDone`,`completedAt`,`sortOrder`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: HabitEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.title)
        statement.bindText(3, entity.iconKey)
        statement.bindText(4, entity.detail)
        val _tmpScheduledMinutes: Int? = entity.scheduledMinutes
        if (_tmpScheduledMinutes == null) {
          statement.bindNull(5)
        } else {
          statement.bindLong(5, _tmpScheduledMinutes.toLong())
        }
        statement.bindLong(6, entity.targetCount.toLong())
        statement.bindLong(7, entity.currentCount.toLong())
        val _tmp: Int = if (entity.isDone) 1 else 0
        statement.bindLong(8, _tmp.toLong())
        val _tmpCompletedAt: Long? = entity.completedAt
        if (_tmpCompletedAt == null) {
          statement.bindNull(9)
        } else {
          statement.bindLong(9, _tmpCompletedAt)
        }
        statement.bindLong(10, entity.sortOrder.toLong())
      }
    }
    this.__deleteAdapterOfHabitEntity = object : EntityDeleteOrUpdateAdapter<HabitEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `habits` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: HabitEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfHabitEntity = object : EntityDeleteOrUpdateAdapter<HabitEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `habits` SET `id` = ?,`title` = ?,`iconKey` = ?,`detail` = ?,`scheduledMinutes` = ?,`targetCount` = ?,`currentCount` = ?,`isDone` = ?,`completedAt` = ?,`sortOrder` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: HabitEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.title)
        statement.bindText(3, entity.iconKey)
        statement.bindText(4, entity.detail)
        val _tmpScheduledMinutes: Int? = entity.scheduledMinutes
        if (_tmpScheduledMinutes == null) {
          statement.bindNull(5)
        } else {
          statement.bindLong(5, _tmpScheduledMinutes.toLong())
        }
        statement.bindLong(6, entity.targetCount.toLong())
        statement.bindLong(7, entity.currentCount.toLong())
        val _tmp: Int = if (entity.isDone) 1 else 0
        statement.bindLong(8, _tmp.toLong())
        val _tmpCompletedAt: Long? = entity.completedAt
        if (_tmpCompletedAt == null) {
          statement.bindNull(9)
        } else {
          statement.bindLong(9, _tmpCompletedAt)
        }
        statement.bindLong(10, entity.sortOrder.toLong())
        statement.bindLong(11, entity.id)
      }
    }
  }

  public override suspend fun insert(habit: HabitEntity): Long = performSuspending(__db, false,
      true) { _connection ->
    val _result: Long = __insertAdapterOfHabitEntity.insertAndReturnId(_connection, habit)
    _result
  }

  public override suspend fun insertAll(habits: List<HabitEntity>): Unit = performSuspending(__db,
      false, true) { _connection ->
    __insertAdapterOfHabitEntity.insert(_connection, habits)
  }

  public override suspend fun delete(habit: HabitEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __deleteAdapterOfHabitEntity.handle(_connection, habit)
  }

  public override suspend fun update(habit: HabitEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __updateAdapterOfHabitEntity.handle(_connection, habit)
  }

  public override fun observeAll(): Flow<List<HabitEntity>> {
    val _sql: String = "SELECT * FROM habits ORDER BY isDone ASC, sortOrder ASC"
    return createFlow(__db, false, arrayOf("habits")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfIconKey: Int = getColumnIndexOrThrow(_stmt, "iconKey")
        val _columnIndexOfDetail: Int = getColumnIndexOrThrow(_stmt, "detail")
        val _columnIndexOfScheduledMinutes: Int = getColumnIndexOrThrow(_stmt, "scheduledMinutes")
        val _columnIndexOfTargetCount: Int = getColumnIndexOrThrow(_stmt, "targetCount")
        val _columnIndexOfCurrentCount: Int = getColumnIndexOrThrow(_stmt, "currentCount")
        val _columnIndexOfIsDone: Int = getColumnIndexOrThrow(_stmt, "isDone")
        val _columnIndexOfCompletedAt: Int = getColumnIndexOrThrow(_stmt, "completedAt")
        val _columnIndexOfSortOrder: Int = getColumnIndexOrThrow(_stmt, "sortOrder")
        val _result: MutableList<HabitEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: HabitEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpIconKey: String
          _tmpIconKey = _stmt.getText(_columnIndexOfIconKey)
          val _tmpDetail: String
          _tmpDetail = _stmt.getText(_columnIndexOfDetail)
          val _tmpScheduledMinutes: Int?
          if (_stmt.isNull(_columnIndexOfScheduledMinutes)) {
            _tmpScheduledMinutes = null
          } else {
            _tmpScheduledMinutes = _stmt.getLong(_columnIndexOfScheduledMinutes).toInt()
          }
          val _tmpTargetCount: Int
          _tmpTargetCount = _stmt.getLong(_columnIndexOfTargetCount).toInt()
          val _tmpCurrentCount: Int
          _tmpCurrentCount = _stmt.getLong(_columnIndexOfCurrentCount).toInt()
          val _tmpIsDone: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsDone).toInt()
          _tmpIsDone = _tmp != 0
          val _tmpCompletedAt: Long?
          if (_stmt.isNull(_columnIndexOfCompletedAt)) {
            _tmpCompletedAt = null
          } else {
            _tmpCompletedAt = _stmt.getLong(_columnIndexOfCompletedAt)
          }
          val _tmpSortOrder: Int
          _tmpSortOrder = _stmt.getLong(_columnIndexOfSortOrder).toInt()
          _item =
              HabitEntity(_tmpId,_tmpTitle,_tmpIconKey,_tmpDetail,_tmpScheduledMinutes,_tmpTargetCount,_tmpCurrentCount,_tmpIsDone,_tmpCompletedAt,_tmpSortOrder)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun count(): Int {
    val _sql: String = "SELECT COUNT(*) FROM habits"
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

  public override suspend fun byId(id: Long): HabitEntity? {
    val _sql: String = "SELECT * FROM habits WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfIconKey: Int = getColumnIndexOrThrow(_stmt, "iconKey")
        val _columnIndexOfDetail: Int = getColumnIndexOrThrow(_stmt, "detail")
        val _columnIndexOfScheduledMinutes: Int = getColumnIndexOrThrow(_stmt, "scheduledMinutes")
        val _columnIndexOfTargetCount: Int = getColumnIndexOrThrow(_stmt, "targetCount")
        val _columnIndexOfCurrentCount: Int = getColumnIndexOrThrow(_stmt, "currentCount")
        val _columnIndexOfIsDone: Int = getColumnIndexOrThrow(_stmt, "isDone")
        val _columnIndexOfCompletedAt: Int = getColumnIndexOrThrow(_stmt, "completedAt")
        val _columnIndexOfSortOrder: Int = getColumnIndexOrThrow(_stmt, "sortOrder")
        val _result: HabitEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpIconKey: String
          _tmpIconKey = _stmt.getText(_columnIndexOfIconKey)
          val _tmpDetail: String
          _tmpDetail = _stmt.getText(_columnIndexOfDetail)
          val _tmpScheduledMinutes: Int?
          if (_stmt.isNull(_columnIndexOfScheduledMinutes)) {
            _tmpScheduledMinutes = null
          } else {
            _tmpScheduledMinutes = _stmt.getLong(_columnIndexOfScheduledMinutes).toInt()
          }
          val _tmpTargetCount: Int
          _tmpTargetCount = _stmt.getLong(_columnIndexOfTargetCount).toInt()
          val _tmpCurrentCount: Int
          _tmpCurrentCount = _stmt.getLong(_columnIndexOfCurrentCount).toInt()
          val _tmpIsDone: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsDone).toInt()
          _tmpIsDone = _tmp != 0
          val _tmpCompletedAt: Long?
          if (_stmt.isNull(_columnIndexOfCompletedAt)) {
            _tmpCompletedAt = null
          } else {
            _tmpCompletedAt = _stmt.getLong(_columnIndexOfCompletedAt)
          }
          val _tmpSortOrder: Int
          _tmpSortOrder = _stmt.getLong(_columnIndexOfSortOrder).toInt()
          _result =
              HabitEntity(_tmpId,_tmpTitle,_tmpIconKey,_tmpDetail,_tmpScheduledMinutes,_tmpTargetCount,_tmpCurrentCount,_tmpIsDone,_tmpCompletedAt,_tmpSortOrder)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun resetForNewDay() {
    val _sql: String = "UPDATE habits SET isDone = 0, currentCount = 0, completedAt = NULL"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
