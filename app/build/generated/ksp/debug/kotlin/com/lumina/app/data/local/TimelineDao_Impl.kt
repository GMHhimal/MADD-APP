package com.lumina.app.`data`.local

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.lumina.app.`data`.model.EventKind
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
public class TimelineDao_Impl(
  __db: RoomDatabase,
) : TimelineDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfTimelineEventEntity: EntityInsertAdapter<TimelineEventEntity>

  private val __luminaConverters: LuminaConverters = LuminaConverters()

  private val __updateAdapterOfTimelineEventEntity: EntityDeleteOrUpdateAdapter<TimelineEventEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfTimelineEventEntity = object : EntityInsertAdapter<TimelineEventEntity>()
        {
      protected override fun createQuery(): String =
          "INSERT OR ABORT INTO `timeline_events` (`id`,`startMinutes`,`endMinutes`,`title`,`meta`,`kind`) VALUES (nullif(?, 0),?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: TimelineEventEntity) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.startMinutes.toLong())
        statement.bindLong(3, entity.endMinutes.toLong())
        statement.bindText(4, entity.title)
        statement.bindText(5, entity.meta)
        val _tmp: String = __luminaConverters.kindToString(entity.kind)
        statement.bindText(6, _tmp)
      }
    }
    this.__updateAdapterOfTimelineEventEntity = object :
        EntityDeleteOrUpdateAdapter<TimelineEventEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `timeline_events` SET `id` = ?,`startMinutes` = ?,`endMinutes` = ?,`title` = ?,`meta` = ?,`kind` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: TimelineEventEntity) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.startMinutes.toLong())
        statement.bindLong(3, entity.endMinutes.toLong())
        statement.bindText(4, entity.title)
        statement.bindText(5, entity.meta)
        val _tmp: String = __luminaConverters.kindToString(entity.kind)
        statement.bindText(6, _tmp)
        statement.bindLong(7, entity.id)
      }
    }
  }

  public override suspend fun insertAll(items: List<TimelineEventEntity>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfTimelineEventEntity.insert(_connection, items)
  }

  public override suspend fun insert(item: TimelineEventEntity): Long = performSuspending(__db,
      false, true) { _connection ->
    val _result: Long = __insertAdapterOfTimelineEventEntity.insertAndReturnId(_connection, item)
    _result
  }

  public override suspend fun update(item: TimelineEventEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __updateAdapterOfTimelineEventEntity.handle(_connection, item)
  }

  public override fun observeAll(): Flow<List<TimelineEventEntity>> {
    val _sql: String = "SELECT * FROM timeline_events ORDER BY startMinutes ASC"
    return createFlow(__db, false, arrayOf("timeline_events")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfStartMinutes: Int = getColumnIndexOrThrow(_stmt, "startMinutes")
        val _columnIndexOfEndMinutes: Int = getColumnIndexOrThrow(_stmt, "endMinutes")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfMeta: Int = getColumnIndexOrThrow(_stmt, "meta")
        val _columnIndexOfKind: Int = getColumnIndexOrThrow(_stmt, "kind")
        val _result: MutableList<TimelineEventEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TimelineEventEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpStartMinutes: Int
          _tmpStartMinutes = _stmt.getLong(_columnIndexOfStartMinutes).toInt()
          val _tmpEndMinutes: Int
          _tmpEndMinutes = _stmt.getLong(_columnIndexOfEndMinutes).toInt()
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpMeta: String
          _tmpMeta = _stmt.getText(_columnIndexOfMeta)
          val _tmpKind: EventKind
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfKind)
          _tmpKind = __luminaConverters.stringToKind(_tmp)
          _item =
              TimelineEventEntity(_tmpId,_tmpStartMinutes,_tmpEndMinutes,_tmpTitle,_tmpMeta,_tmpKind)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAll(): List<TimelineEventEntity> {
    val _sql: String = "SELECT * FROM timeline_events ORDER BY startMinutes ASC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfStartMinutes: Int = getColumnIndexOrThrow(_stmt, "startMinutes")
        val _columnIndexOfEndMinutes: Int = getColumnIndexOrThrow(_stmt, "endMinutes")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfMeta: Int = getColumnIndexOrThrow(_stmt, "meta")
        val _columnIndexOfKind: Int = getColumnIndexOrThrow(_stmt, "kind")
        val _result: MutableList<TimelineEventEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TimelineEventEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpStartMinutes: Int
          _tmpStartMinutes = _stmt.getLong(_columnIndexOfStartMinutes).toInt()
          val _tmpEndMinutes: Int
          _tmpEndMinutes = _stmt.getLong(_columnIndexOfEndMinutes).toInt()
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpMeta: String
          _tmpMeta = _stmt.getText(_columnIndexOfMeta)
          val _tmpKind: EventKind
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfKind)
          _tmpKind = __luminaConverters.stringToKind(_tmp)
          _item =
              TimelineEventEntity(_tmpId,_tmpStartMinutes,_tmpEndMinutes,_tmpTitle,_tmpMeta,_tmpKind)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun count(): Int {
    val _sql: String = "SELECT COUNT(*) FROM timeline_events"
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

  public override suspend fun byId(id: Long): TimelineEventEntity? {
    val _sql: String = "SELECT * FROM timeline_events WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfStartMinutes: Int = getColumnIndexOrThrow(_stmt, "startMinutes")
        val _columnIndexOfEndMinutes: Int = getColumnIndexOrThrow(_stmt, "endMinutes")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfMeta: Int = getColumnIndexOrThrow(_stmt, "meta")
        val _columnIndexOfKind: Int = getColumnIndexOrThrow(_stmt, "kind")
        val _result: TimelineEventEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpStartMinutes: Int
          _tmpStartMinutes = _stmt.getLong(_columnIndexOfStartMinutes).toInt()
          val _tmpEndMinutes: Int
          _tmpEndMinutes = _stmt.getLong(_columnIndexOfEndMinutes).toInt()
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpMeta: String
          _tmpMeta = _stmt.getText(_columnIndexOfMeta)
          val _tmpKind: EventKind
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfKind)
          _tmpKind = __luminaConverters.stringToKind(_tmp)
          _result =
              TimelineEventEntity(_tmpId,_tmpStartMinutes,_tmpEndMinutes,_tmpTitle,_tmpMeta,_tmpKind)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun findConflict(
    startMinutes: Int,
    endMinutes: Int,
    excludeId: Long,
  ): TimelineEventEntity? {
    val _sql: String = """
        |
        |        SELECT * FROM timeline_events
        |        WHERE id != ? AND startMinutes < ? AND endMinutes > ?
        |        ORDER BY startMinutes ASC LIMIT 1
        |    
        """.trimMargin()
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, excludeId)
        _argIndex = 2
        _stmt.bindLong(_argIndex, endMinutes.toLong())
        _argIndex = 3
        _stmt.bindLong(_argIndex, startMinutes.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfStartMinutes: Int = getColumnIndexOrThrow(_stmt, "startMinutes")
        val _columnIndexOfEndMinutes: Int = getColumnIndexOrThrow(_stmt, "endMinutes")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfMeta: Int = getColumnIndexOrThrow(_stmt, "meta")
        val _columnIndexOfKind: Int = getColumnIndexOrThrow(_stmt, "kind")
        val _result: TimelineEventEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpStartMinutes: Int
          _tmpStartMinutes = _stmt.getLong(_columnIndexOfStartMinutes).toInt()
          val _tmpEndMinutes: Int
          _tmpEndMinutes = _stmt.getLong(_columnIndexOfEndMinutes).toInt()
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpMeta: String
          _tmpMeta = _stmt.getText(_columnIndexOfMeta)
          val _tmpKind: EventKind
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfKind)
          _tmpKind = __luminaConverters.stringToKind(_tmp)
          _result =
              TimelineEventEntity(_tmpId,_tmpStartMinutes,_tmpEndMinutes,_tmpTitle,_tmpMeta,_tmpKind)
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
