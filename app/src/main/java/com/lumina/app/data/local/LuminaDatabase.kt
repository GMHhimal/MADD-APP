package com.lumina.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        UserProfileEntity::class,
        HabitEntity::class,
        DeadlineEntity::class,
        TimelineEventEntity::class,
        MoodEntryEntity::class,
        HealthReminderEntity::class,
        FocusSessionEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(LuminaConverters::class)
abstract class LuminaDatabase : RoomDatabase() {

    abstract fun profileDao(): ProfileDao
    abstract fun habitDao(): HabitDao
    abstract fun deadlineDao(): DeadlineDao
    abstract fun timelineDao(): TimelineDao
    abstract fun moodDao(): MoodDao
    abstract fun healthReminderDao(): HealthReminderDao
    abstract fun focusSessionDao(): FocusSessionDao

    companion object {
        @Volatile private var instance: LuminaDatabase? = null

        fun get(context: Context): LuminaDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                LuminaDatabase::class.java,
                "lumina.db"
            )
                .fallbackToDestructiveMigration()
                .build()
                .also { instance = it }
        }
    }
}
