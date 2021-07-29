package com.timecat.data.room.habit

import androidx.room.*

@Dao
interface HabitReminderDao {
    @Query("SELECT * FROM HabitReminder WHERE id = :uid LIMIT 1")
    fun getByID(uid: Long): HabitReminder?

    @Query("SELECT * FROM HabitReminder WHERE habitId = :habitId")
    fun getByHabit(habitId: Long): List<HabitReminder>

}