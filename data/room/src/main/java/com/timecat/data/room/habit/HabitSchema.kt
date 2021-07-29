package com.timecat.data.room.habit

import com.timecat.data.room.record.RoomRecord

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/7/20
 * @description null
 * @usage null
 */
object HabitSchema {
    @JvmStatic
    fun getHabit(record: RoomRecord): Habit? = record.habitSchema

    @JvmStatic
    fun isPaused(record: RoomRecord):Boolean = getHabit(record)?.isPaused ?: false
}