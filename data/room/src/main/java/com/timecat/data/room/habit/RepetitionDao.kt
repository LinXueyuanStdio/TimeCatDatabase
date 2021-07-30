package com.timecat.data.room.habit

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.timecat.data.room.BaseDao

@Dao
abstract class RepetitionDao : BaseDao<RoomRepetition>{
    @Insert
    abstract fun newRep(widget: RoomRepetition): Long

    @Query("DELETE FROM RoomRepetition WHERE recordId = :recordId AND timestamp = :timestamp")
    abstract fun remove(recordId: Long, timestamp: Long)

    @Query("DELETE FROM RoomRepetition WHERE recordId = :recordId")
    abstract fun delete(recordId: Long)
}