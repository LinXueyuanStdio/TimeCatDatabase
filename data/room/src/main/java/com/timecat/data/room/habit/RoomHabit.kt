package com.timecat.data.room.habit

import android.content.Context
import com.alibaba.fastjson.JSONObject
import com.timecat.data.room.doing.DoingRecord
import com.timecat.data.room.record.RoomRecord
import com.timecat.data.room.reminder.Reminder

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-05
 * @description null
 * @usage null
 */
var RoomRecord.position: Int
    get() = extension.getInteger("position") ?: 0
    set(value) {
        extension.put("position", value)
    }

fun RoomRecord.getReminder(context: Context): Reminder? {
    return reminderSchema
}

fun RoomRecord.getHabit(context: Context): Habit? {
    return habitSchema
}

var RoomRecord.habitSchema: Habit?
    get() = extension.getJSONObject("habit")?.let {
        val data = Habit.fromJson(it.toJSONString())
        data.id = this.id
        data
    }
    set(value) {
        extension.put("habit", value?.toJsonObject())
    }
var RoomRecord.reminderSchema: Reminder?
    get() = extension.getJSONObject("reminder")?.let {
        val data = Reminder.fromJson(it.toJSONString())
        data.id = this.id
        data
    }
    set(value) {
        extension.put("reminder", value?.toJsonObject())
    }
var RoomRecord.doingRecords: MutableList<DoingRecord>
    get() = extension.getJSONArray("doingRecords")?.let {
        val data = it.map {
            val json = it as? JSONObject
            json?.let {
                DoingRecord.fromJson(it.toJSONString())
            }
        }.filterNotNull().toMutableList()
        data
    } ?: mutableListOf()
    set(value) {
        extension.put("doingRecords", value.map { it.toJsonObject() })
    }