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

    @Transaction
    open fun getDetailByID(uid: Long): String? {
        val data = get(uid)
        return data?.habitSchema?.detail
    }

    @Query("DELETE FROM Habit WHERE id = :id")
    fun delete(id: Long)

    @Query("UPDATE Habit SET intervalInfo = :intervalInfo WHERE id =:id")
    fun updateHabitIntervalInfo(id: Long, intervalInfo: String)

    @Transaction
    fun updateHabitIntervalInfo(record: RoomRecord, intervalInfo: String) {
        val h = record.habitSchema ?: return
        h.intervalInfo = intervalInfo
        record.habitSchema = h
        updateRoomRecords(record)
    }

    @Update
    fun updateRoomRecords(vararg record: RoomRecord): Int

    @Transaction
    fun addHabitIntervalInfo(id: Long, intervalInfoToAdd: String) {
        getByID(id)?.let {
            updateHabitIntervalInfo(id, it.intervalInfo + intervalInfoToAdd)
        }
    }
    @Transaction
    fun addHabitIntervalInfo(record: RoomRecord, intervalInfoToAdd: String) {
        record.habitSchema?.let {
            updateHabitIntervalInfo(record, it.intervalInfo + intervalInfoToAdd)
        }
    }

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

    @Query("UPDATE Habit SET type = :type , remindedTimes = :remindedTimes , detail=:detail , record = :record , intervalInfo = :intervalInfo WHERE id =:id")
    fun updateHabit(
        id: Long,
        type: Int,
        remindedTimes: Int,
        detail: String,
        record: String,
        intervalInfo: String
    )

    //region transaction 1
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHabitReminder(reminder: HabitReminder): Long

    @Transaction
    fun createHabitReminder(habitReminders: List<HabitReminder>, listener: OnTransactionFinish) {
        for (habitReminder in habitReminders) {
            val notifyTime = habitReminder.notifyTime
            insertHabitReminder(HabitReminder(mHabitReminderId, habitReminder.habitId, notifyTime))
            listener.setHabitReminderAlarm(mHabitReminderId, notifyTime)
        }
    }

    @Transaction
    fun createHabit(habit: Habit, listener: OnTransactionFinish) {
        insert(habit)
        for (habitReminder in habit.habitReminders) {
            val notifyTime = habitReminder.notifyTime
            insertHabitReminder(HabitReminder(mHabitReminderId, habitReminder.habitId, notifyTime))
            listener.setHabitReminderAlarm(mHabitReminderId, notifyTime)
        }
    }

    interface OnTransactionFinish {
        fun setHabitReminderAlarm(id: Long, notifyTime: Long)
    }
    //endregion

    //region transaction 2
    @Transaction
    fun deleteHabit(id: Long, listener: OnTransactionDeleteHabitReminder) {
        delete(id)
        getHabitRemindersByHabit(id)?.let {
            listener.deleteHabitReminderAlarm(it)
        }
        deleteHabitReminderByHabit(id)
        deleteHabitRecordByHabit(id)
    }

    @Query("SELECT * FROM HabitReminder WHERE habitId = :habitId")
    fun getHabitRemindersByHabit(habitId: Long): List<HabitReminder>?

    @Query("DELETE FROM HabitReminder WHERE habitId = :habitId")
    fun deleteHabitReminderByHabit(habitId: Long)

    @Query("DELETE FROM HabitRecord WHERE habitId = :habitId")
    fun deleteHabitRecordByHabit(habitId: Long)

    interface OnTransactionDeleteHabitReminder {
        fun deleteHabitReminderAlarm(habitReminders: List<HabitReminder>)
    }

    //endregion
    //region transaction 3
    @Transaction
    fun recreateHabit(id: Long, habit: Habit, listener: OnTransactionRecreateHabit) {
        // 1. delete Habit
        delete(id)
        // 2. delete HabitReminders
        getHabitRemindersByHabit(id)?.let {
            listener.deleteHabitReminderAlarm(it)
        }
        deleteHabitReminderByHabit(id)
        // 3. delete HabitRecords
        deleteHabitRecordByHabit(id)

        // 5. create Habit
        insert(habit)
        // 6. create HabitReminders
        for (habitReminder in habit.habitReminders) {
            val notifyTime = habitReminder.notifyTime
            insertHabitReminder(HabitReminder(mHabitReminderId, habitReminder.habitId, notifyTime))
            listener.setHabitReminderAlarm(mHabitReminderId, notifyTime)
        }
    }

    interface OnTransactionRecreateHabit : OnTransactionDeleteHabitReminder, OnTransactionFinish

    //endregion
    //region transaction 4
    @Transaction
    fun recreateHabitReminders(habit: Habit, listener: OnTransactionRecreateHabitReminders) {
        val id = habit.id
        // 1. update Habit
        updateHabit(
            habit.id,
            habit.type,
            habit.remindedTimes,
            habit.detail,
            habit.record,
            habit.intervalInfo
        );

        // 2. delete HabitReminders
        getHabitRemindersByHabit(id)?.let {
            listener.deleteHabitReminderAlarm(it)
        }
        deleteHabitReminderByHabit(id)

        // 3. reInit HabitReminders
        habit.initHabitReminders()

        // 4. create HabitReminders
        for (habitReminder in habit.habitReminders) {
            val mHabitReminderId = listener.getHabitReminderId()
            val notifyTime = habitReminder.notifyTime
            insertHabitReminder(HabitReminder(mHabitReminderId, habitReminder.habitId, notifyTime))
            listener.setHabitReminderAlarm(mHabitReminderId, notifyTime)
        }
    }

    interface OnTransactionRecreateHabitReminders
        : OnTransactionFinish, OnTransactionDeleteHabitReminder

    //endregion
    //region transaction 5
    @Transaction
    fun getHabit(id: Long): Habit? {
        val habit: Habit = getByID(id) ?: return null
        habit.habitReminders = getHabitRemindersByHabit(id)
        habit.habitRecords = getHabitRecordsByHabit(id)
        return habit
    }

    @Query("SELECT * FROM HabitRecord WHERE habitId = :habitId AND (type = ${HabitRecord.TYPE_FINISHED} or type = ${HabitRecord.TYPE_FAKE_FINISHED}) ORDER BY recordTime ASC")
    fun getHabitRecordsByHabit(habitId: Long): List<HabitRecord>
    //endregion


}
