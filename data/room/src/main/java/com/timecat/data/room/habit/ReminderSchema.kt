package com.timecat.data.room.habit

import com.timecat.data.room.record.RoomRecord
import com.timecat.data.room.reminder.Reminder

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/7/20
 * @description null
 * @usage null
 */
object ReminderSchema {
    @JvmStatic
    fun getReminder(record: RoomRecord): Reminder? = record.reminderSchema
}