package com.timecat.data.room.habit

import androidx.room.*

@Dao
interface HabitRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(widget: HabitRecord): Long

    @Query("SELECT * FROM HabitRecord WHERE habitId = :habitId AND (type = ${HabitRecord.TYPE_FINISHED} or type = ${HabitRecord.TYPE_FAKE_FINISHED}) ORDER BY recordTime DESC LIMIT :limited")
    fun getByHabitLimited(habitId: Long, limited: Int): List<HabitRecord>

    @Query("DELETE FROM HabitRecord WHERE id = :uid")
    fun delete(uid: Long)

}