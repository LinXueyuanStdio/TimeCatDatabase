package com.timecat.data.room.record

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.timecat.data.room.*
import com.timecat.data.room.habit.*
import com.timecat.data.room.reminder.Reminder
import com.timecat.identity.data.base.*
import com.timecat.identity.data.block.type.*
import org.intellij.lang.annotations.Language
import org.joda.time.DateTime
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-11-29
 * @description null
 * @usage null
 */
@Dao
abstract class RecordDao : BaseDao<RoomRecord> {
    //region 通用
    @Query("SELECT * FROM records WHERE id = :id LIMIT 1")
    abstract fun get(id: Long): RoomRecord?

    @Query("SELECT * FROM records WHERE title = :title LIMIT 1")
    abstract fun get(title: String): RoomRecord?

    @Query("SELECT * FROM records WHERE uuid = :uuid")
    abstract fun getByUuid(uuid: String): RoomRecord?

    @Query("SELECT * FROM records WHERE type IN (:type) AND subType IN (:subTypes) AND (status & :status != 0)")
    abstract fun getByDomain(
        type: List<Int>,
        subTypes: List<Int>,
        status: Long
    ): MutableList<RoomRecord>?

    @Query("SELECT * FROM records WHERE type IN (:type) AND (status & :status != 0)")
    abstract fun getByDomain(
        type: List<Int>,
        status: Long
    ): MutableList<RoomRecord>?

    @Query("SELECT * FROM records WHERE type IN (:type) AND (status & :status != 0) AND (status & :falseState = 0)")
    abstract fun getByDomain(
        type: List<Int>,
        status: Long,
        falseState: Long
    ): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type IN (:type) AND (status & :status != 0) AND (status & :falseState = 0) AND parent = :parent")
    abstract fun getByDomain(
        type: List<Int>,
        status: Long,
        falseState: Long,
        parent: String
    ): MutableList<RoomRecord>

    @Transaction
    open fun loadFor(
        parent: String?,
        type: List<Int>,
        status: Long,
        falseState: Long
    ): MutableList<RoomRecord> {
        if (parent == null) {
            return getByDomain(type, status, falseState)
        } else {
            return getByDomain(type, status, falseState, parent)
        }
    }

    @Query("SELECT * FROM records WHERE uuid = :uuid")
    abstract fun getLiveData(uuid: String): LiveData<RoomRecord> // null not allowed, use List

    @Query("SELECT * FROM records WHERE (startTime >= :startTime) AND (startTime <= :endTime)")
    abstract fun getBetween(startTime: Long, endTime: Long): MutableList<RoomRecord>

    @Query("SELECT * FROM records ORDER BY createTime DESC")
    abstract fun getAll(): MutableList<RoomRecord>

    @Query("SELECT count(*) FROM records")
    abstract fun count(): Int

    @Query("UPDATE records SET status = status & $TASK_ARCHIVE WHERE uuid = :uuid")
    abstract fun markArchive(uuid: String)

    @Query("UPDATE records SET status = status & $TASK_DELETE WHERE uuid = :uuid")
    abstract fun markDelete(uuid: String)

    @Transaction
    open fun archiveBatch(uuids: List<String>) {
        uuids.forEach {
            markArchive(it)
        }
    }

    @Transaction
    open fun deleteBatch(uuids: List<String>) {
        uuids.forEach {
            markDelete(it)
        }
    }

    @Query("DELETE FROM records WHERE uuid IN (:uuids)")
    abstract fun hardDeleteBatch(uuids: List<String>)

    @Insert
    abstract fun insertRoomRecords(vararg records: RoomRecord): LongArray

    @Update
    abstract fun updateRoomRecords(vararg record: RoomRecord): Int
    //endregion

    //region BLOCK_COLLECTION
    @Query("SELECT * FROM records WHERE type = $BLOCK_CONTAINER ORDER BY createTime DESC")
    abstract fun getAllContainer(): MutableList<RoomRecord>
    //endregion

    //region BLOCK_CONVERSATION
    @Query("SELECT * FROM records WHERE type = $BLOCK_CONVERSATION ORDER BY updateTime DESC")
    abstract fun getAll_BLOCK_CONVERSATION(): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_CONVERSATION AND parent = :bookUuid LIMIT 1")
    abstract fun getForBook_BLOCK_CONVERSATION(bookUuid: String): RoomRecord?

    @Query("SELECT * FROM records WHERE type = $BLOCK_CONVERSATION AND parent = :bookUuid")
    abstract fun getListForBook_BLOCK_CONVERSATION(bookUuid: String): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_CONVERSATION AND parent = :bookUuid")
    abstract fun getLiveListForBook_BLOCK_CONVERSATION(bookUuid: String): LiveData<MutableList<RoomRecord>>

    @Query("SELECT * FROM records WHERE type = $BLOCK_CONVERSATION AND uuid = :conversationId AND subType = :conversationType")
    abstract fun get_BLOCK_CONVERSATION(conversationId: String, conversationType: Int): RoomRecord?
    //endregion

    //region BLOCK_MESSAGE
    @Query("SELECT * FROM records WHERE type = $BLOCK_MESSAGE ORDER BY createTime")
    abstract fun getAll_BLOCK_MESSAGE(): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_MESSAGE AND parent = :conversationId ORDER BY createTime LIMIT $LIST_SIZE")
    abstract fun getAll_BLOCK_MESSAGE(conversationId: String): MutableList<RoomRecord>

    @Query("SELECT count(*) FROM records WHERE type = $BLOCK_MESSAGE AND parent = :conversationId")
    abstract fun getAllMsgCount_BLOCK_MESSAGE(conversationId: String): Int

    @Query("SELECT * FROM records WHERE parent = :conversationId ORDER BY createTime")
    abstract fun getLatestMessage_BLOCK_MESSAGE(conversationId: String): RoomRecord?

    @Query("SELECT * FROM records WHERE parent = :conversationId AND createTime < :createTime ORDER BY createTime LIMIT :pageSize")
    abstract fun getAll_BLOCK_MESSAGE(
        conversationId: String,
        createTime: Long,
        pageSize: Int
    ): MutableList<RoomRecord>
    //endregion

    //region BLOCK_RECORD
    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND ((status & $TASK_DELETE) = 0) ORDER BY updateTime DESC")
    abstract fun getAll_BLOCK_RECORD(): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND ((status & $TASK_DELETE) = 0) ORDER BY updateTime DESC")
    abstract fun getAllLive_BLOCK_RECORD(): LiveData<List<RoomRecord>>

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND subType IN ($REMINDER, $HABIT, $GOAL) AND ((status & $TASK_MODE_FALSE) = 0) ORDER BY updateTime DESC")
    abstract fun getAllTime_BLOCK_RECORD(): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND subType IN ($REMINDER, $HABIT, $GOAL) AND  ( (startTime<=:fromTs AND (startTime+totalLength >= :toTs)) OR (startTime>=:fromTs AND (startTime+totalLength <= :toTs)) OR (startTime<=:fromTs AND (startTime+totalLength >= :fromTs)) OR (startTime<=:toTs AND (startTime+totalLength >= :toTs))  ) AND ((status & $TASK_MODE_FALSE) = 0) ORDER BY updateTime DESC")
    abstract fun getAllTime_BLOCK_RECORD(
        fromTs: Long,
        toTs: Long
    ): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE parent=:uuid AND (status & $TASK_DELETE) = 0")
    abstract fun getAllLiveChildren(uuid: String): MutableList<RoomRecord>

    @Transaction
    open fun getLiveChildrenTree(record: RoomRecord, exist: MutableList<String>): TreeRecord? {
        if (record.uuid in exist) return null
        exist.add(record.uuid)
        val ans = TreeRecord(record, mutableListOf())
        val children = getAllLiveChildren(record.uuid)
        if (children.isEmpty()) return ans
        ans.children.addAll(children.map { getLiveChildrenTree(it, exist) }.filterNotNull())
        return ans
    }

    @Query("SELECT * FROM records WHERE parent=:uuid")
    abstract fun getAllChildren(uuid: String): MutableList<RoomRecord>

    @Transaction
    open fun getChildrenTree(record: RoomRecord, exist: MutableList<String>): TreeRecord? {
        if (record.uuid in exist) return null
        exist.add(record.uuid)
        val ans = TreeRecord(record, mutableListOf())
        val children = getAllChildren(record.uuid)
        if (children.isEmpty()) return ans
        ans.children.addAll(children.map { getChildrenTree(it, exist) }.filterNotNull())
        return ans
    }

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND subType IN (:subTypes) AND render_type = $RENDER_TYPE_Record AND ((status & :status) != 0) ORDER BY updateTime DESC")
    abstract fun getForDisplay_BLOCK_RECORD(
        subTypes: List<Int>,
        status: Int
    ): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND subType =:subType AND render_type = $RENDER_TYPE_Record ORDER BY updateTime DESC")
    abstract fun getAllForDisplay_BLOCK_RECORD(subType: Int): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND render_type = $RENDER_TYPE_Record ORDER BY updateTime DESC")
    abstract fun getAllForDisplay_BLOCK_RECORD(): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND subType IN (:subTypes) AND render_type = $RENDER_TYPE_Record AND ((status & :status) != 0) AND ((status & :falseStatus) = 0) ORDER BY updateTime DESC")
    abstract fun getForDisplay_BLOCK_RECORD(
        subTypes: List<Int>,
        status: Long,
        falseStatus: Long
    ): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND subType IN (:subTypes) AND render_type = $RENDER_TYPE_Record AND ((status & :status) != 0) AND ((status & :falseStatus) = 0) ORDER BY updateTime DESC")
    abstract fun getCursorForDisplay_BLOCK_RECORD(
        subTypes: List<Int>,
        status: Long,
        falseStatus: Long
    ): Cursor

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND (subType IN (:subTypes) AND render_type = $RENDER_TYPE_Record AND ((status & :status) != 0) ) AND ((status & :falseStatus) = 0)  AND color = :color AND (title LIKE '%' || :keyword || '%' OR content LIKE '%' || :keyword || '%') ORDER BY `order` DESC")
    abstract fun getForKeywordDisplay_BLOCK_RECORD(
        subTypes: List<Int>,
        status: Long,
        falseStatus: Long,
        keyword: String,
        color: Int
    ): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND (subType IN (:subTypes) AND render_type = $RENDER_TYPE_Record AND ((status & :status) != 0) ) AND ((status & :falseStatus) = 0)  AND (title LIKE '%' || :keyword || '%' OR content LIKE '%' || :keyword || '%') ORDER BY `order` DESC")
    abstract fun getForKeywordDisplay_BLOCK_RECORD(
        subTypes: List<Int>,
        status: Long,
        falseStatus: Long,
        keyword: String
    ): MutableList<RoomRecord>

    @Query("SELECT id, subType FROM records WHERE type = $BLOCK_RECORD AND render_type = $RENDER_TYPE_Record AND ((status & $TASK_UNDERWAY) != 0) AND ((status & $TASK_DELETE) = 0)")
    abstract fun getAllUnderWay(): MutableList<IdAndType>

    data class IdAndType(val id: Long, val subType: Int)

    @Transaction
    open fun createAllAlarms(
        updateHabitRemindedTimes: Boolean,
        forceToUpdateRemindedTimes: Boolean,
        listener: OnCreateAllAlarms,
        notify: OnUpdateNotifyTime
    ) {
        val cursor: List<IdAndType> = getAllUnderWay()
        for ((id, type) in cursor) {
            if (type == REMINDER || type == GOAL) {
                val reminder: Reminder? = getReminderById(id)
                if (reminder == null || !reminder.isUnderWay) {
                    continue
                }
                val notifyTime = reminder.getNotifyTime()
                if (notifyTime < System.currentTimeMillis()) {
                    reminder.setState(Reminder.EXPIRED)
                    updateReminder(reminder)
                } else {
                    listener.sendAlarmBroadcast(id, notifyTime)
                }
            } else if (type == HABIT) {
                // 直接将习惯的提醒时间更新到最新时刻
                // 当用户收到提醒但未完成一次，备份应用，并在下一个周期恢复时，该习惯将不会为这一次添加记录"0"
                updateHabitToLatest(id, updateHabitRemindedTimes, false, notify)
            }
        }
        listener.end()
    }

    @Transaction
    open fun updateHabitRemindedTimes(id: Long, remindedTimes: Int) {
        val roomRecord = get(id) ?: return
        updateHabitRemindedTimes(roomRecord, remindedTimes)
    }

    @Transaction
    open fun updateHabitRemindedTimes(roomRecord: RoomRecord, remindedTimes: Int) {
        val habit = roomRecord.habitSchema ?: return
        habit.remindedTimes = remindedTimes
        roomRecord.habitSchema = habit
        update(roomRecord)
    }

    @Transaction
    open fun updateHabitToLatest(
        id: Long,
        updateRemindedTimes: Boolean,
        forceToUpdateRemindedTimes: Boolean,
        notify: OnUpdateNotifyTime
    ) {
        // This may happen if the universe boom, so we should consider it strictly.
        val habit = getHabit(id) ?: return

        val recordTimes = habit.record.length
        if (updateRemindedTimes && forceToUpdateRemindedTimes) {
            // This will prevent this habit from finishing in this T if it was notified but
            // user didn't finish it at once.
            updateHabitRemindedTimes(id, recordTimes)
        }

        var habitReminders = habit.habitReminders
        val hrIds: MutableList<Long> = ArrayList()
        for (habitReminder in habitReminders) {
            hrIds.add(habitReminder.createTime)
        }

        habit.initHabitReminders() // habitReminders have become latest.

        // You may see that Habit#initHabitReminders() will also set member firstTime again,
        // but this makes no change here because we don't update habit to database.

        // You may see that Habit#initHabitReminders() will also set member firstTime again,
        // but this makes no change here because we don't update habit to database.
        habitReminders = habit.habitReminders
        val hrIdsSize = hrIds.size
        if (hrIdsSize != habitReminders.size) {
            /**
             * it seems that this cannot happen but it did happen according to a user log.
             * I've tried to solve this problem by ensuring old habit is deleted successfully
             * when updating a habit in DetailActivity.
             * See [com.timecat.module.everything.activities.DetailActivity.setOrUpdateHabit]
             * and [.deleteHabit] for more details.
             * Maybe I didn't actually solve it. Let's see if there are more logs about that.
             */
            return
        }
        updateNotifyTime(hrIds, habitReminders, notify)

        // 将已经提前完成的habitReminder更新至新的周期里

        // 将已经提前完成的habitReminder更新至新的周期里
        val habitType = habit.type
        val habitRecordsThisT = habit.habitRecordsThisT
        for (habitRecord in habitRecordsThisT) {
            val hr: HabitReminder? = habit.getHabitReminderByCreateTime(habitRecord.habitReminderCreateTime)
            hr?.let {
                val now = System.currentTimeMillis()
                val gap = DateTimeUtil.calculateTimeGap(now, hr.notifyTime, habitType)
                if (gap == 0) {
                    updateHabitReminderToNext(habit, hr, notify)
                }
            }
        }

        if (updateRemindedTimes && !forceToUpdateRemindedTimes) {
            val remindedTimes = habit.remindedTimes
            if (recordTimes < remindedTimes) {
                val minTime = habit.minHabitReminderTime
                val maxTime = habit.finalHabitReminder.notifyTime
                val maxLastTime: Long = DateTimeUtil.getHabitReminderTime(habitType, maxTime, -1)
                val curTime = System.currentTimeMillis()
                if (curTime in (maxLastTime + 1) until minTime) {
                    if (DateTimeUtil.calculateTimeGap(maxLastTime, curTime, habitType) != 0) {
                        updateHabitRemindedTimes(id, recordTimes)
                    }
                    // else 用户还能“补”掉这一次未完成的情况，因此不更新remindedTimes
                } else {
                    updateHabitRemindedTimes(id, recordTimes)
                }
            } else if (recordTimes > remindedTimes) {
                updateHabitRemindedTimes(id, recordTimes)
            }
        }
    }

    interface OnCreateAllAlarms {
        fun sendAlarmBroadcast(id: Long, notifyTime: Long)
        fun end()
    }


    @Query("UPDATE HabitReminder SET notifyTime= :notifyTime WHERE id =:id")
    abstract fun updateNotifyTime(id: Long, notifyTime: Long)

    @Transaction
    open fun updateNotifyTime(
        ids: List<Long>,
        habitReminders: List<HabitReminder>,
        notify: OnUpdateNotifyTime
    ) {
        for (i in ids.indices) {
            val newTime: Long = habitReminders.get(i).notifyTime
            val id = ids[i]
            updateNotifyTime(id, newTime)
            notify.updateHabitReminder(id, newTime)
        }
    }


    /**
     * 这次的提醒已完成，计算出下一次的提醒时间
     *
     * @param hrId 习惯 id
     */
    open fun updateHabitReminderToNext(
        habit: Habit,
        habitReminder: HabitReminder,
        notify: OnUpdateNotifyTime
    ) {
        val type = habit.type
        var time = habitReminder.notifyTime

        // do one time before loop if user finish a habit for 1 time in advance
        time = DateTimeUtil.getHabitReminderTime(type, time, 1)
        while (time < System.currentTimeMillis()) {
            time = DateTimeUtil.getHabitReminderTime(type, time, 1)
        }
        notify.updateHabitReminder(habitReminder.habitId, habitReminder.createTime, time)
    }

    interface OnUpdateNotifyTime {
        fun updateHabitReminder(id: Long, hrId: Long, notifyTime: Long)
    }

    @Query("SELECT `order` FROM records ORDER BY `order` ASC LIMIT 1")
    abstract fun getMinLocation(): Int

    @Query("SELECT `order` FROM records ORDER BY `order` DESC LIMIT 1")
    abstract fun getMaxLocation(): Int

    @Query("UPDATE records SET `order` = :location WHERE id = :id")
    abstract fun updateLocation(id: Long, location: Int)

    @Transaction
    open fun updateLocations(ids: List<Long>, locations: List<Int>) {
        for (i in ids.indices) {
            updateLocation(ids[i], locations[i])
        }
    }

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND subType=:subType")
    abstract fun getAllBySubType_BLOCK_RECORD(subType: Int): Cursor

    @Query("SELECT title, content, attachmentItems_attachmentItems FROM records WHERE type = $BLOCK_RECORD AND subType=$NOTE")
    abstract fun getAllTitleContentAttachments_BLOCK_RECORD(): List<TitleContentAttachments>

    data class TitleContentAttachments(
        val title: String,
        val content: String,
        val attachmentItems_attachmentItems: MutableList<AttachmentItem>
    )

    @Query("SELECT count(*) FROM records WHERE type = $BLOCK_RECORD AND subType=:subType AND (status & :status > 0)")
    abstract fun getCount_BLOCK_RECORD(subType: Int, status: Long): Int

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND subType=$HABIT AND (status & $TASK_UNDERWAY > 0)")
    abstract fun getAllHabitUnderway_BLOCK_RECORD(): List<RoomRecord>
    //endregion

    //region BLOCK_BOOK, BLOCK_PAGE
    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND parent = :bookUuid ORDER BY createTime LIMIT $LIST_SIZE")
    abstract fun getAll_BLOCK_PAGE(bookUuid: String): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND parent = :bookUuid AND (status & $TASK_DELETE) = 0 ORDER BY createTime LIMIT $LIST_SIZE")
    abstract fun getAllForBook_BLOCK_PAGE(bookUuid: String): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_CONTAINER ORDER BY createTime LIMIT $LIST_SIZE")
    abstract fun getAll_BLOCK_BOOK(): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_CONTAINER AND parent = :parentUuid ORDER BY createTime LIMIT $LIST_SIZE")
    abstract fun getAll_BLOCK_BOOK(parentUuid: String): MutableList<RoomRecord>
    //endregion

    //region BLOCK_COLLECTION
    @Query("SELECT * FROM records WHERE type = $BLOCK_CONTAINER ORDER BY createTime LIMIT $LIST_SIZE")
    abstract fun getAll_BLOCK_COLLECTION(): MutableList<RoomRecord>
    //endregion

    //region Books
    @Transaction
    open fun update(list: List<RoomRecord>) {
        for (i in list) {
            update(i)
        }
    }

    @Transaction
    open fun markAsDeleted(list: List<RoomRecord>) {
        for (i in list) {
            i.setDeleted(true)
        }
        update(list)
    }

    @Transaction
    open fun markAsEx(list: List<RoomRecord>) {
        for (i in list) {
            i.setDeleted(true)
        }
        update(list)
    }

    @Transaction
    open fun reIndex(list: List<RoomRecord>) {
        list.forEachIndexed { index, roomRecord ->
            if (roomRecord.order != index) {
                roomRecord.order = index
                update(roomRecord)
            }
        }
    }

    @Transaction
    open fun rebuildOrder(list: List<RoomRecord>, from: Int, to: Int, offset: Int) {
        list.subList(from, to).let {
            it.forEach {
                it.order += offset
            }
            update(it)
        }
    }
    //endregion

    //region Things
    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND subType = $HABIT ORDER BY `order` DESC")
    abstract fun getAllHabit(): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND subType = $REMINDER ORDER BY `order` DESC")
    abstract fun getAllReminder(): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND subType = $GOAL ORDER BY `order` DESC")
    abstract fun getAllGoal(): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND subType = $NOTE ORDER BY `order` DESC")
    abstract fun getAllNote(): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_CONTAINER ORDER BY `order` DESC")
    abstract fun getAllBook(): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_CONTAINER ORDER BY `order` DESC")
    abstract fun getAllPage(): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND (status & :status != 0)")
    abstract fun getByState(status: Long): MutableList<RoomRecord>?

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND subType IN (:subTypes) AND ((status & :status) != 0) AND ((status & :falseStatus) = 0) AND createTime < :before ORDER BY updateTime DESC")
    abstract fun getForDisplay_BLOCK_RECORD(
        subTypes: List<Int>,
        status: Long,
        falseStatus: Long,
        before: Long
    ): MutableList<RoomRecord>

    @Transaction
    open fun getBetween_BLOCK_RECORD(date: DateTime): MutableList<RoomRecord> {
        val endTime = date.withMillisOfDay(0).plusDays(1).millis
        //1. can finish but no finish, after created
        return getForDisplay_BLOCK_RECORD(
            listOf(
//                NOTE,
                REMINDER,
                HABIT,
                GOAL
            ),
            TASK_UNDERWAY or TASK_CAN_FINISH,
            TASK_MODE_FALSE,
            endTime
        )
    }

    @Transaction
    open fun getAllHabitData(listener: OnHabitDataLoaded) {
        val all = getAllHabit()
        all.sortBy { it.updateTime }
        for (i in all) {
            i.habitSchema?.let {
                listener.onLoadHabit(i, it)
            }
        }
    }

    interface OnHabitDataLoaded {
        fun onLoadHabit(record: RoomRecord, habit: Habit)
    }

    interface OnReminderDataLoaded {
        fun onLoadReminder(record: RoomRecord, reminder: Reminder)
    }

    interface OnNoteDataLoaded {
        fun onLoadNote(record: RoomRecord)
    }

    interface OnGoalDataLoaded {
        fun onLoadGoal(record: RoomRecord, reminder: Reminder)
    }
    //endregion

    //region rebuildHabitOrder
    @Transaction
    open fun rebuildHabitOrder() {
        val list = getAllHabit()
        var pos = 0
        for (r in list) {
            r.order = pos++
            replace(r)
        }
    }
    //endregion

    //region Reminder
    @Transaction
    open fun getReminderById(uid: Long): Reminder? {
        val record = get(uid)
        return record?.reminderSchema
    }

    @Transaction
    open fun deleteReminderById(uid: Long) {
        val record = get(uid)
        record?.let {
            it.reminderSchema = null
            update(it)
        }
    }

    @Transaction
    open fun updateReminder(reminder: Reminder) {
        val record = get(reminder.id)
        record?.let {
            it.reminderSchema = reminder
            update(it)
        }
    }

    @Transaction
    open fun updateReminder(record: RoomRecord, reminder: Reminder) {
        record.reminderSchema = reminder
        update(record)
    }
    //endregion

    //region Habit
    @Transaction
    open fun getHabitById(uid: Long): Habit? {
        val record = get(uid)
        return record?.habitSchema
    }

    //endregion

    //region Things
    @Transaction
    open fun getRecordData(date: DateTime, listener: OnRecordDataLoaded) {
        val all = getBetween_BLOCK_RECORD(date)
        all.sortBy { it.subType }
        for (i in all) {
            when (i.subType) {
                NOTE -> listener.onLoadNote(i)
                REMINDER -> i.reminderSchema?.let {
                    listener.onLoadReminder(i, it)
                }
                HABIT -> i.habitSchema?.let {
                    listener.onLoadHabit(i, it)
                }
                GOAL -> i.reminderSchema?.let {
                    listener.onLoadGoal(i, it)
                }
                else -> listener.onLoadUnknown(i)
            }
        }
    }

    @Transaction
    open fun getAllRecordData(listener: OnRecordDataLoaded) {
        val all = getAll_BLOCK_RECORD()
        all.sortBy { it.subType }
        for (i in all) {
            when (i.subType) {
                NOTE -> listener.onLoadNote(i)
                REMINDER -> i.reminderSchema?.let {
                    listener.onLoadReminder(i, it)
                }
                HABIT -> i.habitSchema?.let {
                    listener.onLoadHabit(i, it)
                }
                GOAL -> i.reminderSchema?.let {
                    listener.onLoadGoal(i, it)
                }
                else -> listener.onLoadUnknown(i)
            }
        }
    }

    @Transaction
    open fun getAllTimeRecordData(fromTs: Long, toTs: Long, listener: OnTimeRecordDataLoaded) {
        val all = getAllTime_BLOCK_RECORD(fromTs, toTs)
        all.sortBy { it.subType }
        getAllTimeRecordData(all, listener)
    }

    @Transaction
    open fun getAllTimeRecordData(listener: OnTimeRecordDataLoaded) {
        val all = getAllTime_BLOCK_RECORD()
        all.sortBy { it.subType }
        getAllTimeRecordData(all, listener)
    }

    @Transaction
    open fun getAllTimeRecordData(all: List<RoomRecord>, listener: OnTimeRecordDataLoaded) {
        for (i in all) {
            when (i.subType) {
                REMINDER -> i.reminderSchema?.let {
                    listener.onLoadReminder(i, it)
                }
                HABIT -> i.habitSchema?.let {
                    listener.onLoadHabit(i, it)
                }
                GOAL -> i.reminderSchema?.let {
                    listener.onLoadGoal(i, it)
                }
                else -> listener.onLoadUnknown(i)
            }
        }
    }

    @Transaction
    open fun getAllRecordData(all: List<RoomRecord>, listener: OnRecordDataLoaded) {
        for (i in all) {
            when (i.subType) {
                NOTE -> listener.onLoadNote(i)
                REMINDER -> i.reminderSchema?.let {
                    listener.onLoadReminder(i, it)
                }
                HABIT -> i.habitSchema?.let {
                    listener.onLoadHabit(i, it)
                }
                GOAL -> i.reminderSchema?.let {
                    listener.onLoadGoal(i, it)
                }
                else -> listener.onLoadUnknown(i)
            }
        }
    }

    @Transaction
    open fun getAllData(all: List<RoomRecord>, listener: OnDataLoaded) {
        for (i in all) {
            when (i.type) {
                BLOCK_RECORD -> {
                    when (i.subType) {
                        NOTE -> listener.onLoadNote(i)
                        REMINDER -> i.reminderSchema?.let {
                            listener.onLoadReminder(i, it)
                        }
                        HABIT -> i.habitSchema?.let {
                            listener.onLoadHabit(i, it)
                        }
                        GOAL -> i.reminderSchema?.let {
                            listener.onLoadGoal(i, it)
                        }
                        else -> listener.onLoadUnknown(i)
                    }
                }
                BLOCK_DATABASE -> {
                    listener.onLoadDatabase(i)
                }
                BLOCK_MARKDOWN -> {
                    when (i.subType) {
                        TEXT -> listener.onLoadText(i)
                        TODO_LIST -> listener.onLoadTodoList(i)
                        Heading1 -> listener.onLoadHeading1(i)
                        Heading2 -> listener.onLoadHeading2(i)
                        Heading3 -> listener.onLoadHeading3(i)
                        Heading4 -> listener.onLoadHeading4(i)
                        Heading5 -> listener.onLoadHeading5(i)
                        Heading6 -> listener.onLoadHeading6(i)
                        BULLETED_LIST -> listener.onLoadBulletedList(i)
                        NUMBERED_LIST -> listener.onLoadNumberedList(i)
                        TOGGLE_LIST -> {
                            val tree = getLiveChildrenTree(i, mutableListOf())
                            listener.onLoadToggleList(i, tree!!)
                        }
                        QUOTE -> listener.onLoadQuote(i)
                        DIVIDER -> listener.onLoadDivider(i)
                        CALLOUT -> listener.onLoadCallout(i)
                        else -> listener.onLoadUnknown(i)
                    }
                }
                BLOCK_CONVERSATION -> {
                    listener.onLoadConversation(i)
                }
                BLOCK_CONTAINER -> {
                    listener.onLoadContainer(i)
                }
                BLOCK_LINK -> {
                    val linkedUuid = i.title
                    val linkedRecord = getByUuid(linkedUuid)
                    listener.onLoadLink(i, linkedRecord)
                }
                BLOCK_PATH -> {
                    listener.onLoadPath(i)
                }
                BLOCK_BUTTON -> {
                    listener.onLoadButton(i)
                }
                BLOCK_MEDIA -> {
                    listener.onLoadMedia(i)
                }
                BLOCK_MESSAGE -> {
                    listener.onLoadMessage(i)
                }
                else -> listener.onLoadUnknown(i)
            }

        }
    }

    @Transaction
    open fun getHabit(id: Long): Habit? {
        val habit: Habit = getHabitById(id) ?: return null
        return habit
    }

    @Transaction
    open fun getHabit(uuid: String, onHabitDataLoaded: OnHabitDataLoaded) {
        val record = getByUuid(uuid) ?: return
        if (record.type == BLOCK_RECORD && record.subType == HABIT) {
            val habit: Habit = getHabitById(record.id) ?: return
            onHabitDataLoaded.onLoadHabit(record, habit)
        }
    }

    @Query("SELECT * FROM HabitRecord WHERE habitId = :habitId AND (type = ${HabitRecord.TYPE_FINISHED} or type = ${HabitRecord.TYPE_FAKE_FINISHED}) ORDER BY recordTime ASC")
    abstract fun getHabitRecordsByHabit(habitId: Long): List<HabitRecord>

    interface OnDataLoaded : OnUnknownLoaded, OnConversationLoaded, OnMessageLoaded,
        OnHabitDataLoaded, OnReminderDataLoaded, OnNoteDataLoaded, OnGoalDataLoaded, OnContainerLoaded,
        OnLinkLoaded, OnPathLoaded, OnButtonLoaded, OnBasicLoaded, OnMediaLoaded, OnDatabaseLoaded

    interface OnTimeRecordDataLoaded
        : OnUnknownLoaded, OnHabitDataLoaded, OnReminderDataLoaded, OnGoalDataLoaded

    interface OnRecordDataLoaded
        : OnUnknownLoaded, OnHabitDataLoaded, OnReminderDataLoaded, OnNoteDataLoaded, OnGoalDataLoaded

    interface OnUnknownLoaded {
        fun onLoadUnknown(record: RoomRecord)
    }

    interface OnConversationLoaded {
        fun onLoadConversation(record: RoomRecord)
    }

    interface OnMessageLoaded {
        fun onLoadMessage(record: RoomRecord)
    }

    interface OnContainerLoaded {
        fun onLoadContainer(record: RoomRecord)
    }

    interface OnLinkLoaded {
        fun onLoadLink(record: RoomRecord, linkedRecord: RoomRecord?)
    }

    interface OnPathLoaded {
        fun onLoadPath(record: RoomRecord)
    }

    interface OnButtonLoaded {
        fun onLoadButton(record: RoomRecord)
    }

    interface OnMediaLoaded {
        fun onLoadMedia(record: RoomRecord)
    }

    interface OnDatabaseLoaded {
        fun onLoadDatabase(record: RoomRecord)
    }

    interface OnBasicLoaded {
        fun onLoadText(record: RoomRecord)
        fun onLoadTodoList(record: RoomRecord)
        fun onLoadHeading1(record: RoomRecord)
        fun onLoadHeading2(record: RoomRecord)
        fun onLoadHeading3(record: RoomRecord)
        fun onLoadHeading4(record: RoomRecord)
        fun onLoadHeading5(record: RoomRecord)
        fun onLoadHeading6(record: RoomRecord)
        fun onLoadBulletedList(record: RoomRecord)
        fun onLoadNumberedList(record: RoomRecord)
        fun onLoadToggleList(record: RoomRecord, list: TreeRecord)
        fun onLoadQuote(record: RoomRecord)
        fun onLoadDivider(record: RoomRecord)
        fun onLoadCallout(record: RoomRecord)
    }
    //endregion

    //region SimpleRecord
    @Query("SELECT uuid, type, subType, title FROM records WHERE uuid = :uuid LIMIT 1")
    abstract fun getSimpleRecordByUuid(uuid: String): SimpleRecord?

    @Query("SELECT id FROM records WHERE uuid = :uuid LIMIT 1")
    abstract fun getIdByUuid(uuid: String): Long

    data class SimpleRecord(val uuid: String, val type: Int, val subType: Int, val title: String)
    //endregion

    //region search
    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND ((status & $TASK_DELETE) = 0) ORDER BY updateTime DESC LIMIT :pageSize OFFSET :offset")
    abstract fun getAll_BLOCK_RECORD(offset: Int, pageSize: Int): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_RECORD AND subType IN ($REMINDER, $HABIT, $GOAL) AND  ( (startTime<=:fromTs AND (startTime+totalLength >= :toTs)) OR (startTime>=:fromTs AND (startTime+totalLength <= :toTs)) OR (startTime<=:fromTs AND (startTime+totalLength >= :fromTs)) OR (startTime<=:toTs AND (startTime+totalLength >= :toTs))  ) AND ((status & $TASK_MODE_FALSE) = 0) ORDER BY updateTime DESC LIMIT :pageSize OFFSET :offset")
    abstract fun getAllTime_BLOCK_RECORD(
        fromTs: Long,
        toTs: Long,
        offset: Int,
        pageSize: Int
    ): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE parent=:uuid AND (status & $TASK_DELETE) = 0 LIMIT :pageSize OFFSET :offset")
    abstract fun getAllLiveChildren(uuid: String, offset: Int, pageSize: Int): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = :type AND subType = :subType ORDER BY `order` DESC LIMIT :pageSize OFFSET :offset")
    abstract fun getAllByTypeAndSubtype(type: Int, subType: Int, offset: Int, pageSize: Int): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE type = :type AND subType = :subType ORDER BY `order` DESC")
    abstract fun getAllByTypeAndSubtype(type: Int, subType: Int): MutableList<RoomRecord>

    @Query("SELECT * FROM records WHERE title LIKE :query OR content LIKE :query OR name LIKE :query LIMIT :pageSize OFFSET :offset")
    abstract fun searchAll(query: String, offset: Int, pageSize: Int): MutableList<RoomRecord>
    //endregion

    @RawQuery
    abstract fun exec(q: SupportSQLiteQuery): MutableList<RoomRecord>

    //region Template
    @Query("SELECT * FROM records WHERE (status & $TASK_TEMPLATE != 0) ORDER BY createTime LIMIT $LIST_SIZE")
    abstract fun getAllTemplate(): MutableList<RoomRecord>
    //endregion

    companion object {
        const val LIST_SIZE = 512

        @Language("RoomSql")
        const val ORDER_BY_USER = """
            ORDER BY ((status & $TASK_PIN) = 1) DESC, (CASE :order 
                when 0 then `order`
                when 1 then createTime
                when 2 then updateTime
                when 3 then title
                when 4 then type
                ELSE `order`
            end) (IF(:asc, ASC, DESC))
        """
    }
}