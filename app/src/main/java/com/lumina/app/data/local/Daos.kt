package com.lumina.app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun observe(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE id = 1")
    suspend fun get(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(profile: UserProfileEntity)
}

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits ORDER BY isDone ASC, sortOrder ASC")
    fun observeAll(): Flow<List<HabitEntity>>

    @Query("SELECT COUNT(*) FROM habits")
    suspend fun count(): Int

    @Insert suspend fun insert(habit: HabitEntity): Long
    @Insert suspend fun insertAll(habits: List<HabitEntity>)
    @Update suspend fun update(habit: HabitEntity)
    @Delete suspend fun delete(habit: HabitEntity)

    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun byId(id: Long): HabitEntity?

    @Query("UPDATE habits SET isDone = 0, currentCount = 0, completedAt = NULL")
    suspend fun resetForNewDay()
}

@Dao
interface DeadlineDao {
    @Query("SELECT * FROM deadlines ORDER BY dueAt ASC")
    fun observeAll(): Flow<List<DeadlineEntity>>

    @Query("SELECT COUNT(*) FROM deadlines") suspend fun count(): Int
    @Insert suspend fun insert(deadline: DeadlineEntity): Long
    @Insert suspend fun insertAll(items: List<DeadlineEntity>)
    @Update suspend fun update(deadline: DeadlineEntity)
    @Delete suspend fun delete(deadline: DeadlineEntity)

    @Query("SELECT * FROM deadlines WHERE id = :id")
    suspend fun byId(id: Long): DeadlineEntity?
}

@Dao
interface TimelineDao {
    @Query("SELECT * FROM timeline_events ORDER BY startMinutes ASC")
    fun observeAll(): Flow<List<TimelineEventEntity>>

    @Query("SELECT * FROM timeline_events ORDER BY startMinutes ASC")
    suspend fun getAll(): List<TimelineEventEntity>

    @Query("SELECT COUNT(*) FROM timeline_events") suspend fun count(): Int
    @Insert suspend fun insertAll(items: List<TimelineEventEntity>)
    @Insert suspend fun insert(item: TimelineEventEntity): Long
    @Update suspend fun update(item: TimelineEventEntity)

    @Query("SELECT * FROM timeline_events WHERE id = :id")
    suspend fun byId(id: Long): TimelineEventEntity?

    /** Any event that overlaps the given window, ignoring [excludeId]. */
    @Query("""
        SELECT * FROM timeline_events
        WHERE id != :excludeId AND startMinutes < :endMinutes AND endMinutes > :startMinutes
        ORDER BY startMinutes ASC LIMIT 1
    """)
    suspend fun findConflict(startMinutes: Int, endMinutes: Int, excludeId: Long): TimelineEventEntity?
}

@Dao
interface MoodDao {
    @Query("SELECT * FROM mood_entries ORDER BY recordedAt DESC")
    fun observeAll(): Flow<List<MoodEntryEntity>>

    @Query("SELECT * FROM mood_entries ORDER BY recordedAt DESC LIMIT 1")
    fun observeLatest(): Flow<MoodEntryEntity?>

    @Query("SELECT COUNT(*) FROM mood_entries") suspend fun count(): Int
    @Insert suspend fun insert(entry: MoodEntryEntity): Long
    @Insert suspend fun insertAll(items: List<MoodEntryEntity>)
}

@Dao
interface HealthReminderDao {
    @Query("SELECT * FROM health_reminders ORDER BY nextDueAt ASC")
    fun observeAll(): Flow<List<HealthReminderEntity>>

    @Query("SELECT COUNT(*) FROM health_reminders") suspend fun count(): Int
    @Insert suspend fun insert(reminder: HealthReminderEntity): Long
    @Insert suspend fun insertAll(items: List<HealthReminderEntity>)
    @Update suspend fun update(reminder: HealthReminderEntity)
    @Delete suspend fun delete(reminder: HealthReminderEntity)
}

@Dao
interface FocusSessionDao {
    @Query("SELECT * FROM focus_sessions ORDER BY startedAt DESC")
    fun observeAll(): Flow<List<FocusSessionEntity>>

    @Query("SELECT COUNT(*) FROM focus_sessions") suspend fun count(): Int
    @Insert suspend fun insert(session: FocusSessionEntity): Long
    @Insert suspend fun insertAll(items: List<FocusSessionEntity>)
}
