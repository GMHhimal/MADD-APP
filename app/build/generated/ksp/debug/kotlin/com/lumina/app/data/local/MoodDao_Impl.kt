package com.lumina.app.`data`.local

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.lumina.app.`data`.model.Mood
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
public class MoodDao_Impl(
  __db: RoomDatabase,
) : MoodDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfMoodEntryEntity: EntityInsertAdapter<MoodEntryEntity>

  private val __luminaConverters: LuminaConverters = LuminaConverters()
  init {
    this.__db = __db
    this.__insertAdapterOfMoodEntryEntity = object : EntityInsertAdapter<MoodEntryEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR ABORT INTO `mood_entries` (`id`,`recordedAt`,`mood`,`causes`) VALUES (nullif(?, 0),?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: MoodEntryEntity) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.recordedAt)
        val _tmp: String = __luminaConverters.moodToString(entity.mood)
        statement.bindText(3, _tmp)
        val _tmp_1: String = __luminaConverters.stringListToString(entity.causes)
        statement.bindText(4, _tmp_1)
      }
    }
  }

  public override suspend fun insert(entry: MoodEntryEntity): Long = performSuspending(__db, false,
      true) { _connection ->
    val _result: Long = __insertAdapterOfMoodEntryEntity.insertAndReturnId(_connection, entry)
    _result
  }

  public override suspend fun insertAll(items: List<MoodEntryEntity>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfMoodEntryEntity.insert(_connection, items)
  }

  public override fun observeAll(): Flow<List<MoodEntryEntity>> {
    val _sql: String = "SELECT * FROM mood_entries ORDER BY recordedAt DESC"
    return createFlow(__db, false, arrayOf("mood_entries")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfRecordedAt: Int = getColumnIndexOrThrow(_stmt, "recordedAt")
        val _columnIndexOfMood: Int = getColumnIndexOrThrow(_stmt, "mood")
        val _columnIndexOfCauses: Int = getColumnIndexOrThrow(_stmt, "causes")
        val _result: MutableList<MoodEntryEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: MoodEntryEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpRecordedAt: Long
          _tmpRecordedAt = _stmt.getLong(_columnIndexOfRecordedAt)
          val _tmpMood: Mood
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfMood)
          _tmpMood = __luminaConverters.stringToMood(_tmp)
          val _tmpCauses: List<String>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfCauses)
          _tmpCauses = __luminaConverters.stringToStringList(_tmp_1)
          _item = MoodEntryEntity(_tmpId,_tmpRecordedAt,_tmpMood,_tmpCauses)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeLatest(): Flow<MoodEntryEntity?> {
    val _sql: String = "SELECT * FROM mood_entries ORDER BY recordedAt DESC LIMIT 1"
    return createFlow(__db, false, arrayOf("mood_entries")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfRecordedAt: Int = getColumnIndexOrThrow(_stmt, "recordedAt")
        val _columnIndexOfMood: Int = getColumnIndexOrThrow(_stmt, "mood")
        val _columnIndexOfCauses: Int = getColumnIndexOrThrow(_stmt, "causes")
        val _result: MoodEntryEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpRecordedAt: Long
          _tmpRecordedAt = _stmt.getLong(_columnIndexOfRecordedAt)
          val _tmpMood: Mood
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfMood)
          _tmpMood = __luminaConverters.stringToMood(_tmp)
          val _tmpCauses: List<String>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfCauses)
          _tmpCauses = __luminaConverters.stringToStringList(_tmp_1)
          _result = MoodEntryEntity(_tmpId,_tmpRecordedAt,_tmpMood,_tmpCauses)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun count(): Int {
    val _sql: String = "SELECT COUNT(*) FROM mood_entries"
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
