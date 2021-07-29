package com.timecat.data.room.habit

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface HabitRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(widget: HabitRecord): Long

}