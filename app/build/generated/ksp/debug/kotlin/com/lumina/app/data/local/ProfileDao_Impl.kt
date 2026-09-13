package com.lumina.app.`data`.local

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class ProfileDao_Impl(
  __db: RoomDatabase,
) : ProfileDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfUserProfileEntity: EntityInsertAdapter<UserProfileEntity>

  private val __luminaConverters: LuminaConverters = LuminaConverters()
  init {
    this.__db = __db
    this.__insertAdapterOfUserProfileEntity = object : EntityInsertAdapter<UserProfileEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `user_profile` (`id`,`displayName`,`role`,`goals`,`interests`,`preferredFreeTime`,`wakeUpMinutes`,`sleepMinutes`,`breakfastMinutes`,`lunchMinutes`,`dinnerMinutes`,`exerciseMinutes`,`exerciseDays`,`smartSuggestions`,`onboardingComplete`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: UserProfileEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.displayName)
        statement.bindText(3, entity.role)
        val _tmp: String = __luminaConverters.stringListToString(entity.goals)
        statement.bindText(4, _tmp)
        val _tmp_1: String = __luminaConverters.stringListToString(entity.interests)
        statement.bindText(5, _tmp_1)
        statement.bindText(6, entity.preferredFreeTime)
        statement.bindLong(7, entity.wakeUpMinutes.toLong())
        statement.bindLong(8, entity.sleepMinutes.toLong())
        statement.bindLong(9, entity.breakfastMinutes.toLong())
        statement.bindLong(10, entity.lunchMinutes.toLong())
        statement.bindLong(11, entity.dinnerMinutes.toLong())
        statement.bindLong(12, entity.exerciseMinutes.toLong())
        val _tmp_2: String = __luminaConverters.stringListToString(entity.exerciseDays)
        statement.bindText(13, _tmp_2)
        val _tmp_3: Int = if (entity.smartSuggestions) 1 else 0
        statement.bindLong(14, _tmp_3.toLong())
        val _tmp_4: Int = if (entity.onboardingComplete) 1 else 0
        statement.bindLong(15, _tmp_4.toLong())
      }
    }
  }

  public override suspend fun upsert(profile: UserProfileEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __insertAdapterOfUserProfileEntity.insert(_connection, profile)
  }

  public override fun observe(): Flow<UserProfileEntity?> {
    val _sql: String = "SELECT * FROM user_profile WHERE id = 1"
    return createFlow(__db, false, arrayOf("user_profile")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfDisplayName: Int = getColumnIndexOrThrow(_stmt, "displayName")
        val _columnIndexOfRole: Int = getColumnIndexOrThrow(_stmt, "role")
        val _columnIndexOfGoals: Int = getColumnIndexOrThrow(_stmt, "goals")
        val _columnIndexOfInterests: Int = getColumnIndexOrThrow(_stmt, "interests")
        val _columnIndexOfPreferredFreeTime: Int = getColumnIndexOrThrow(_stmt, "preferredFreeTime")
        val _columnIndexOfWakeUpMinutes: Int = getColumnIndexOrThrow(_stmt, "wakeUpMinutes")
        val _columnIndexOfSleepMinutes: Int = getColumnIndexOrThrow(_stmt, "sleepMinutes")
        val _columnIndexOfBreakfastMinutes: Int = getColumnIndexOrThrow(_stmt, "breakfastMinutes")
        val _columnIndexOfLunchMinutes: Int = getColumnIndexOrThrow(_stmt, "lunchMinutes")
        val _columnIndexOfDinnerMinutes: Int = getColumnIndexOrThrow(_stmt, "dinnerMinutes")
        val _columnIndexOfExerciseMinutes: Int = getColumnIndexOrThrow(_stmt, "exerciseMinutes")
        val _columnIndexOfExerciseDays: Int = getColumnIndexOrThrow(_stmt, "exerciseDays")
        val _columnIndexOfSmartSuggestions: Int = getColumnIndexOrThrow(_stmt, "smartSuggestions")
        val _columnIndexOfOnboardingComplete: Int = getColumnIndexOrThrow(_stmt,
            "onboardingComplete")
        val _result: UserProfileEntity?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpDisplayName: String
          _tmpDisplayName = _stmt.getText(_columnIndexOfDisplayName)
          val _tmpRole: String
          _tmpRole = _stmt.getText(_columnIndexOfRole)
          val _tmpGoals: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfGoals)
          _tmpGoals = __luminaConverters.stringToStringList(_tmp)
          val _tmpInterests: List<String>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfInterests)
          _tmpInterests = __luminaConverters.stringToStringList(_tmp_1)
          val _tmpPreferredFreeTime: String
          _tmpPreferredFreeTime = _stmt.getText(_columnIndexOfPreferredFreeTime)
          val _tmpWakeUpMinutes: Int
          _tmpWakeUpMinutes = _stmt.getLong(_columnIndexOfWakeUpMinutes).toInt()
          val _tmpSleepMinutes: Int
          _tmpSleepMinutes = _stmt.getLong(_columnIndexOfSleepMinutes).toInt()
          val _tmpBreakfastMinutes: Int
          _tmpBreakfastMinutes = _stmt.getLong(_columnIndexOfBreakfastMinutes).toInt()
          val _tmpLunchMinutes: Int
          _tmpLunchMinutes = _stmt.getLong(_columnIndexOfLunchMinutes).toInt()
          val _tmpDinnerMinutes: Int
          _tmpDinnerMinutes = _stmt.getLong(_columnIndexOfDinnerMinutes).toInt()
          val _tmpExerciseMinutes: Int
          _tmpExerciseMinutes = _stmt.getLong(_columnIndexOfExerciseMinutes).toInt()
          val _tmpExerciseDays: List<String>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfExerciseDays)
          _tmpExerciseDays = __luminaConverters.stringToStringList(_tmp_2)
          val _tmpSmartSuggestions: Boolean
          val _tmp_3: Int
          _tmp_3 = _stmt.getLong(_columnIndexOfSmartSuggestions).toInt()
          _tmpSmartSuggestions = _tmp_3 != 0
          val _tmpOnboardingComplete: Boolean
          val _tmp_4: Int
          _tmp_4 = _stmt.getLong(_columnIndexOfOnboardingComplete).toInt()
          _tmpOnboardingComplete = _tmp_4 != 0
          _result =
              UserProfileEntity(_tmpId,_tmpDisplayName,_tmpRole,_tmpGoals,_tmpInterests,_tmpPreferredFreeTime,_tmpWakeUpMinutes,_tmpSleepMinutes,_tmpBreakfastMinutes,_tmpLunchMinutes,_tmpDinnerMinutes,_tmpExerciseMinutes,_tmpExerciseDays,_tmpSmartSuggestions,_tmpOnboardingComplete)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun `get`(): UserProfileEntity? {
    val _sql: String = "SELECT * FROM user_profile WHERE id = 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfDisplayName: Int = getColumnIndexOrThrow(_stmt, "displayName")
        val _columnIndexOfRole: Int = getColumnIndexOrThrow(_stmt, "role")
        val _columnIndexOfGoals: Int = getColumnIndexOrThrow(_stmt, "goals")
        val _columnIndexOfInterests: Int = getColumnIndexOrThrow(_stmt, "interests")
        val _columnIndexOfPreferredFreeTime: Int = getColumnIndexOrThrow(_stmt, "preferredFreeTime")
        val _columnIndexOfWakeUpMinutes: Int = getColumnIndexOrThrow(_stmt, "wakeUpMinutes")
        val _columnIndexOfSleepMinutes: Int = getColumnIndexOrThrow(_stmt, "sleepMinutes")
        val _columnIndexOfBreakfastMinutes: Int = getColumnIndexOrThrow(_stmt, "breakfastMinutes")
        val _columnIndexOfLunchMinutes: Int = getColumnIndexOrThrow(_stmt, "lunchMinutes")
        val _columnIndexOfDinnerMinutes: Int = getColumnIndexOrThrow(_stmt, "dinnerMinutes")
        val _columnIndexOfExerciseMinutes: Int = getColumnIndexOrThrow(_stmt, "exerciseMinutes")
        val _columnIndexOfExerciseDays: Int = getColumnIndexOrThrow(_stmt, "exerciseDays")
        val _columnIndexOfSmartSuggestions: Int = getColumnIndexOrThrow(_stmt, "smartSuggestions")
        val _columnIndexOfOnboardingComplete: Int = getColumnIndexOrThrow(_stmt,
            "onboardingComplete")
        val _result: UserProfileEntity?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpDisplayName: String
          _tmpDisplayName = _stmt.getText(_columnIndexOfDisplayName)
          val _tmpRole: String
          _tmpRole = _stmt.getText(_columnIndexOfRole)
          val _tmpGoals: List<String>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfGoals)
          _tmpGoals = __luminaConverters.stringToStringList(_tmp)
          val _tmpInterests: List<String>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfInterests)
          _tmpInterests = __luminaConverters.stringToStringList(_tmp_1)
          val _tmpPreferredFreeTime: String
          _tmpPreferredFreeTime = _stmt.getText(_columnIndexOfPreferredFreeTime)
          val _tmpWakeUpMinutes: Int
          _tmpWakeUpMinutes = _stmt.getLong(_columnIndexOfWakeUpMinutes).toInt()
          val _tmpSleepMinutes: Int
          _tmpSleepMinutes = _stmt.getLong(_columnIndexOfSleepMinutes).toInt()
          val _tmpBreakfastMinutes: Int
          _tmpBreakfastMinutes = _stmt.getLong(_columnIndexOfBreakfastMinutes).toInt()
          val _tmpLunchMinutes: Int
          _tmpLunchMinutes = _stmt.getLong(_columnIndexOfLunchMinutes).toInt()
          val _tmpDinnerMinutes: Int
          _tmpDinnerMinutes = _stmt.getLong(_columnIndexOfDinnerMinutes).toInt()
          val _tmpExerciseMinutes: Int
          _tmpExerciseMinutes = _stmt.getLong(_columnIndexOfExerciseMinutes).toInt()
          val _tmpExerciseDays: List<String>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfExerciseDays)
          _tmpExerciseDays = __luminaConverters.stringToStringList(_tmp_2)
          val _tmpSmartSuggestions: Boolean
          val _tmp_3: Int
          _tmp_3 = _stmt.getLong(_columnIndexOfSmartSuggestions).toInt()
          _tmpSmartSuggestions = _tmp_3 != 0
          val _tmpOnboardingComplete: Boolean
          val _tmp_4: Int
          _tmp_4 = _stmt.getLong(_columnIndexOfOnboardingComplete).toInt()
          _tmpOnboardingComplete = _tmp_4 != 0
          _result =
              UserProfileEntity(_tmpId,_tmpDisplayName,_tmpRole,_tmpGoals,_tmpInterests,_tmpPreferredFreeTime,_tmpWakeUpMinutes,_tmpSleepMinutes,_tmpBreakfastMinutes,_tmpLunchMinutes,_tmpDinnerMinutes,_tmpExerciseMinutes,_tmpExerciseDays,_tmpSmartSuggestions,_tmpOnboardingComplete)
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
