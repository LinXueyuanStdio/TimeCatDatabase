package com.timecat.data.room.habit

import androidx.room.*
import com.timecat.data.room.record.RoomRecord

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-17
 * @description null
 * @usage null
 */

@Dao
interface HabitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(widget: Habit): Long

    @Delete
    fun delete(tag: Habit)


    @Query("SELECT * FROM records WHERE id = :id LIMIT 1")
    abstract fun get(id: Long): RoomRecord?

    @Transaction
    open fun getByID(uid: Long): Habit? {
        val data = get(uid)
        return data?.habitSchema
    }

    @Query("DELETE FROM Habit WHERE id = :id")
    fun delete(id: Long)

    @Query("UPDATE Habit SET intervalInfo = :intervalInfo WHERE id =:id")
    fun updateHabitIntervalInfo(id: Long, intervalInfo: String)

    @Transaction
    fun removeLastHabitIntervalInfo(id: Long) {
        getByID(id)?.let {
            var interval = it.intervalInfo
            interval = interval.substring(
                0,
                interval.lastIndexOf(if (interval.endsWith(";")) "," else ";") + 1
            )
            updateHabitIntervalInfo(id, interval)
        }
    }

}
