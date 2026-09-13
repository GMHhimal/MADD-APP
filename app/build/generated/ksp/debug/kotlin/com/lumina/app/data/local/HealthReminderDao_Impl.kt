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
public class HealthReminderDao_Impl(
  __db: RoomDatabase,
) : HealthReminderDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfHealthReminderEntity: EntityInsertAdapter<HealthReminderEntity>

  private val __deleteAdapterOfHealthReminderEntity:
      EntityDeleteOrUpdateAdapter<HealthReminderEntity>

  private val __updateAdapterOfHealthReminderEntity:
      EntityDeleteOrUpdateAdapter<HealthReminderEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfHealthReminderEntity = object :
        EntityInsertAdapter<HealthReminderEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR ABORT INTO `health_reminders` (`id`,`title`,`iconKey`,`lastDoneAt`,`nextDueAt`,`repeatMonths`,`note`,`notifyEnabled`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: HealthReminderEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.title)
        statement.bindText(3, entity.iconKey)
        val _tmpLastDoneAt: Long? = entity.lastDoneAt
        if (_tmpLastDoneAt == null) {
          statement.bindNull(4)
        } else {
          statement.bindLong(4, _tmpLastDoneAt)
        }
        statement.bindLong(5, entity.nextDueAt)
        statement.bindLong(6, entity.repeatMonths.toLong())
        statement.bindText(7, entity.note)
        val _tmp: Int = if (entity.notifyEnabled) 1 else 0
        statement.bindLong(8, _tmp.toLong())
      }
    }
    this.__deleteAdapterOfHealthReminderEntity = object :
        EntityDeleteOrUpdateAdapter<HealthReminderEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `health_reminders` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: HealthReminderEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfHealthReminderEntity = object :
        EntityDeleteOrUpdateAdapter<HealthReminderEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `health_reminders` SET `id` = ?,`title` = ?,`iconKey` = ?,`lastDoneAt` = ?,`nextDueAt` = ?,`repeatMonths` = ?,`note` = ?,`notifyEnabled` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: HealthReminderEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.title)
        statement.bindText(3, entity.iconKey)
        val _tmpLastDoneAt: Long? = entity.lastDoneAt
        if (_tmpLastDoneAt == null) {
          statement.bindNull(4)
        } else {
          statement.bindLong(4, _tmpLastDoneAt)
        }
        statement.bindLong(5, entity.nextDueAt)
        statement.bindLong(6, entity.repeatMonths.toLong())
        statement.bindText(7, entity.note)
        val _tmp: Int = if (entity.notifyEnabled) 1 else 0
        statement.bindLong(8, _tmp.toLong())
        statement.bindLong(9, entity.id)
      }
    }
  }

  public override suspend fun insert(reminder: HealthReminderEntity): Long = performSuspending(__db,
      false, true) { _connection ->
    val _result: Long = __insertAdapterOfHealthReminderEntity.insertAndReturnId(_connection,
        reminder)
    _result
  }

  public override suspend fun insertAll(items: List<HealthReminderEntity>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfHealthReminderEntity.insert(_connection, items)
  }

  public override suspend fun delete(reminder: HealthReminderEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __deleteAdapterOfHealthReminderEntity.handle(_connection, reminder)
  }

  public override suspend fun update(reminder: HealthReminderEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __updateAdapterOfHealthReminderEntity.handle(_connection, reminder)
  }

  public override fun observeAll(): Flow<List<HealthReminderEntity>> {
    val _sql: String = "SELECT * FROM health_reminders ORDER BY nextDueAt ASC"
    return createFlow(__db, false, arrayOf("health_reminders")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfIconKey: Int = getColumnIndexOrThrow(_stmt, "iconKey")
        val _columnIndexOfLastDoneAt: Int = getColumnIndexOrThrow(_stmt, "lastDoneAt")
        val _columnIndexOfNextDueAt: Int = getColumnIndexOrThrow(_stmt, "nextDueAt")
        val _columnIndexOfRepeatMonths: Int = getColumnIndexOrThrow(_stmt, "repeatMonths")
        val _columnIndexOfNote: Int = getColumnIndexOrThrow(_stmt, "note")
        val _columnIndexOfNotifyEnabled: Int = getColumnIndexOrThrow(_stmt, "notifyEnabled")
        val _result: MutableList<HealthReminderEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: HealthReminderEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpIconKey: String
          _tmpIconKey = _stmt.getText(_columnIndexOfIconKey)
          val _tmpLastDoneAt: Long?
          if (_stmt.isNull(_columnIndexOfLastDoneAt)) {
            _tmpLastDoneAt = null
          } else {
            _tmpLastDoneAt = _stmt.getLong(_columnIndexOfLastDoneAt)
          }
          val _tmpNextDueAt: Long
          _tmpNextDueAt = _stmt.getLong(_columnIndexOfNextDueAt)
          val _tmpRepeatMonths: Int
          _tmpRepeatMonths = _stmt.getLong(_columnIndexOfRepeatMonths).toInt()
          val _tmpNote: String
          _tmpNote = _stmt.getText(_columnIndexOfNote)
          val _tmpNotifyEnabled: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfNotifyEnabled).toInt()
          _tmpNotifyEnabled = _tmp != 0
          _item =
              HealthReminderEntity(_tmpId,_tmpTitle,_tmpIconKey,_tmpLastDoneAt,_tmpNextDueAt,_tmpRepeatMonths,_tmpNote,_tmpNotifyEnabled)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun count(): Int {
    val _sql: String = "SELECT COUNT(*) FROM health_reminders"
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

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
