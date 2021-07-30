package com.timecat.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.jess.arms.utils.LogUtils
import com.timecat.component.setting.FILE
import com.timecat.data.room.doing.DoingRecord
import com.timecat.data.room.habit.Habit
import com.timecat.data.room.habit.HabitRecord
import com.timecat.data.room.habit.HabitReminder
import com.timecat.data.room.record.*
import com.timecat.data.room.reminder.Reminder
import com.timecat.data.room.tag.Tag
import com.timecat.data.room.tag.TagDao

/**
 * 时光猫超空间
 */
@Database(
    entities = [
        RoomRecord::class,//export
//        DoingRecord::class,//export
//        Reminder::class,//export
        Tag::class,//export
//        RoomRepetition::class,//export
//        Habit::class,//export
//        HabitRecord::class,//export
//        HabitReminder::class,//export
    ],
    version = TimeCatRoomDatabase.EXPORT_VERSION,
    exportSchema = true
)
@TypeConverters(TypeConverter::class, JsonTypeConverter::class)
abstract class TimeCatRoomDatabase : RoomDatabase() {
    abstract fun recordDao(): RecordDao
    abstract fun tagDao(): TagDao

    abstract fun noteDao(): NoteDao
    abstract fun folderDao(): FolderDao
    abstract fun transDao(): TransDao

    companion object {
        const val NAME = "timecat_room.db"
        const val EXPORT_VERSION = 10

        const val NAME_FOR_TESTS = "test_timecat_room.db"

        @JvmStatic
        fun forMemory(context: Context): TimeCatRoomDatabase {
            return Room.inMemoryDatabaseBuilder(
                context.applicationContext,
                TimeCatRoomDatabase::class.java
            )
                .allowMainThreadQueries()
                .build()
        }

        @JvmStatic
        var instanceMap: MutableMap<String, TimeCatRoomDatabase> = mutableMapOf()

        @JvmStatic
        @JvmOverloads
        fun forFile(context: Context, fileName: String = FILE.RoomDatabase.fileName): TimeCatRoomDatabase {
            var instance = instanceMap.get(fileName)
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext, TimeCatRoomDatabase::class.java, fileName
                )
                    .allowMainThreadQueries() // TODO: Remove
                    .fallbackToDestructiveMigration()
                    .addMigrations(
                        MIGRATION_2_3,
                        MIGRATION_3_4,
                        MIGRATION_4_5,
                        MIGRATION_5_6,
                        MIGRATION_6_7,
                        MIGRATION_7_8,
                        MIGRATION_8_9,
                        MIGRATION_9_10,
                    )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            LogUtils.debugInfo("onCreateDatabase: filename=${fileName}\n${db.path}, ${db.attachedDbs}, version=${db.version}")
                        }

                        override fun onOpen(db: SupportSQLiteDatabase) {
                            LogUtils.debugInfo("onOpenDatabase: filename=${fileName}\n${db.path}, ${db.attachedDbs}, version=${db.version}")
                        }
                    })
                    .build()
                instanceMap.put(fileName, instance)
            }
            return instance
        }

        @JvmField
        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.beginTransaction()
                database.execSQL("DROP TABLE RoomCheckMark")
                database.execSQL("DROP TABLE RoomScore")
                database.execSQL("DROP TABLE RoomStreak")
                database.setTransactionSuccessful()
                database.endTransaction()
            }
        }

        fun execAlterRenameColume(
            oldTable: String, database: SupportSQLiteDatabase,
            onExec: (database: SupportSQLiteDatabase, oldTable: String, newTable: String) -> Unit
        ) {
            val newTable = oldTable + "_new"
            onExec(database, oldTable, newTable)
            database.execSQL("DROP TABLE $oldTable")
            database.execSQL("ALTER TABLE $newTable RENAME TO $oldTable")
        }

        @JvmField
        val MIGRATION_3_4: Migration = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.beginTransaction()
                execAlterRenameColume("RoomRepetition", database) { _, oldTable, newTable ->
                    // TODO 未验证 之前发的版本3里 RoomRepetition 只有3列，但是schemas被更新到和4的相同，没办法写测试
                    // 只能重新安装之前发布的包，然后升级到最新版来测试是否执行成功
                    // 好麻烦，下次一定
                    database.execSQL("CREATE TABLE IF NOT EXISTS $newTable (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `recordId` INTEGER NOT NULL, `timestamp` INTEGER NOT NULL, `value` INTEGER NOT NULL)")
                    database.execSQL("INSERT INTO $newTable (id, recordId, timestamp, value) SELECT id, recordId, timestamp, 0 FROM $oldTable")
                    database.execSQL("CREATE  INDEX `index_${newTable}_id` ON `${newTable}` (`id`)")
                }

                execAlterRenameColume("HabitReminder", database) { _, oldTable, newTable ->
                    database.execSQL("CREATE TABLE IF NOT EXISTS $newTable (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `habitId` INTEGER NOT NULL, `notifyTime` INTEGER NOT NULL)")
                    database.execSQL("INSERT INTO $newTable SELECT * FROM $oldTable")
                    database.execSQL("CREATE  INDEX `index_${newTable}_id` ON `$newTable` (`id`)")
                }

                execAlterRenameColume("HabitRecord", database) { _, oldTable, newTable ->
                    database.execSQL("CREATE TABLE IF NOT EXISTS $newTable (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `habitId` INTEGER NOT NULL, `habitReminderId` INTEGER NOT NULL, `recordTime` INTEGER NOT NULL, `recordYear` INTEGER NOT NULL, `recordMonth` INTEGER NOT NULL, `recordWeek` INTEGER NOT NULL, `recordDay` INTEGER NOT NULL, `type` INTEGER NOT NULL)")
                    database.execSQL("INSERT INTO $newTable SELECT * FROM $oldTable")
                    database.execSQL("CREATE  INDEX `index_${newTable}_id` ON `$newTable` (`id`)")
                }

                execAlterRenameColume("Habit", database) { _, oldTable, newTable ->
                    database.execSQL("CREATE TABLE IF NOT EXISTS $newTable (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `type` INTEGER NOT NULL, `remindedTimes` INTEGER NOT NULL, `detail` TEXT, `record` TEXT, `intervalInfo` TEXT, `createTime` INTEGER NOT NULL, `firstTime` INTEGER NOT NULL)")
                    database.execSQL("INSERT INTO $newTable SELECT * FROM $oldTable")
                    database.execSQL("CREATE  INDEX `index_${newTable}_id` ON `$newTable` (`id`)")
                }

                execAlterRenameColume("Widget", database) { _, oldTable, newTable ->
                    database.execSQL("CREATE TABLE IF NOT EXISTS $newTable (`mId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `mThingId` INTEGER NOT NULL, `mSize` INTEGER NOT NULL, `mAlpha` INTEGER NOT NULL, `mStyle` INTEGER NOT NULL)")
                    database.execSQL("INSERT INTO $newTable SELECT * FROM $oldTable")
                    database.execSQL("CREATE  INDEX `index_${newTable}_mId` ON `$newTable` (`mId`)")
                }

                execAlterRenameColume("Reminder", database) { _, oldTable, newTable ->
                    database.execSQL("CREATE TABLE IF NOT EXISTS $newTable (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `notifyTime` INTEGER NOT NULL, `state` INTEGER NOT NULL, `notifyMillis` INTEGER NOT NULL, `createTime` INTEGER NOT NULL, `updateTime` INTEGER NOT NULL)")
                    database.execSQL("INSERT INTO $newTable SELECT * FROM $oldTable")
                    database.execSQL("CREATE  INDEX `index_${newTable}_id` ON `$newTable` (`id`)")
                }
                database.setTransactionSuccessful()
                database.endTransaction()
            }
        }

        @JvmField
        val MIGRATION_4_5: Migration = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.beginTransaction()
                database.execSQL("CREATE TABLE IF NOT EXISTS note_widget (`widgetId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `noteUUID` TEXT)")
                database.execSQL("CREATE  INDEX `index_note_widget_widgetId` ON note_widget (`widgetId`)")
                database.setTransactionSuccessful()
                database.endTransaction()
            }
        }

        @JvmField
        val MIGRATION_5_6: Migration = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.beginTransaction()

                var TABLE_NAME = "event_types"
                database.execSQL("CREATE TABLE IF NOT EXISTS `${TABLE_NAME}` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `title` TEXT NOT NULL, `color` INTEGER NOT NULL, `caldav_calendar_id` INTEGER NOT NULL, `caldav_display_name` TEXT NOT NULL, `caldav_email` TEXT NOT NULL)")
                database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_event_types_id` ON `${TABLE_NAME}` (`id`)")
                TABLE_NAME = "import_export"
                database.execSQL("CREATE TABLE IF NOT EXISTS `${TABLE_NAME}` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `event_type_id` INTEGER NOT NULL, `record_id` INTEGER NOT NULL)")
                database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_import_export_id` ON `${TABLE_NAME}` (`id`)")
                TABLE_NAME = "import_record"
                database.execSQL("CREATE TABLE IF NOT EXISTS `${TABLE_NAME}` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `record_id` INTEGER NOT NULL, `attendees` TEXT NOT NULL, `import_id` TEXT NOT NULL, `source` TEXT NOT NULL)")
                database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_import_record_id` ON `${TABLE_NAME}` (`id`)")

                var VIEW_NAME = "view_book"
                database.execSQL("CREATE VIEW `${VIEW_NAME}` AS SELECT * FROM records WHERE type = 15")
                VIEW_NAME = "view_dialog"
                database.execSQL("CREATE VIEW `${VIEW_NAME}` AS SELECT * FROM records WHERE type = 14")
                VIEW_NAME = "view_event"
                database.execSQL("CREATE VIEW `${VIEW_NAME}` AS SELECT records.*, import_export.event_type_id, import_record.attendees, import_record.import_id, import_record.source FROM records LEFT JOIN import_record ON (import_record.record_id = records.id) LEFT JOIN import_export ON (import_export.record_id = records.id) WHERE records.type = 0 AND records.subType IN(1, 2, 3)")
                VIEW_NAME = "view_markdown"
                database.execSQL("CREATE VIEW `${VIEW_NAME}` AS SELECT * FROM records WHERE type = 0 AND render_type = 1")
                VIEW_NAME = "view_page"
                database.execSQL("CREATE VIEW `${VIEW_NAME}` AS SELECT * FROM records WHERE type = 16")
                VIEW_NAME = "view_thing"
                database.execSQL("CREATE VIEW `${VIEW_NAME}` AS SELECT * FROM records WHERE type = 0")

                database.setTransactionSuccessful()
                database.endTransaction()
            }
        }

        @JvmField
        val MIGRATION_6_7: Migration = object : Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.beginTransaction()
                database.execSQL("ALTER TABLE records ADD COLUMN icon TEXT NOT NULL DEFAULT 'R.drawable.ic_notes_hint_24dp'")
                database.execSQL("UPDATE records SET coverImageUrl = 'R.drawable.ic_comment' WHERE coverImageUrl = 'R.drawable.dim_ic_chat'")
                database.execSQL("UPDATE records SET icon = coverImageUrl WHERE coverImageUrl IS NOT NULL AND coverImageUrl != ''")
                database.setTransactionSuccessful()
                database.endTransaction()
            }
        }

        @JvmField
        val MIGRATION_7_8: Migration = object : Migration(7, 8) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.beginTransaction()

                var VIEW_NAME = "view_page"
                database.execSQL("DROP VIEW `${VIEW_NAME}`")
                database.execSQL("CREATE VIEW `${VIEW_NAME}` AS SELECT * FROM records WHERE type = 15")
                VIEW_NAME = "view_markdown"
                database.execSQL("DROP VIEW `${VIEW_NAME}`")
                database.execSQL("CREATE VIEW `${VIEW_NAME}` AS SELECT * FROM records WHERE type = 3")

                database.setTransactionSuccessful()
                database.endTransaction()
            }
        }

        @JvmField
        val MIGRATION_8_9: Migration = object : Migration(8, 9) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.beginTransaction()

                var VIEW_NAME = "view_book"
                database.execSQL("DROP VIEW `${VIEW_NAME}`")
                VIEW_NAME = "view_dialog"
                database.execSQL("DROP VIEW `${VIEW_NAME}`")
                VIEW_NAME = "view_event"
                database.execSQL("DROP VIEW `${VIEW_NAME}`")

                var TABLE_NAME = "users"
                database.execSQL("DROP TABLE `${TABLE_NAME}`")
                TABLE_NAME = "Widget"
                database.execSQL("DROP TABLE `${TABLE_NAME}`")
                TABLE_NAME = "event_types"
                database.execSQL("DROP TABLE `${TABLE_NAME}`")
                TABLE_NAME = "import_export"
                database.execSQL("DROP TABLE `${TABLE_NAME}`")
                TABLE_NAME = "import_record"
                database.execSQL("DROP TABLE `${TABLE_NAME}`")
                TABLE_NAME = "note_widget"
                database.execSQL("DROP TABLE `${TABLE_NAME}`")
                TABLE_NAME = "records"
                execAlterRenameColume(TABLE_NAME, database) { db, oldTable, newTable ->
                    database.execSQL(
                        "CREATE TABLE IF NOT EXISTS `${newTable}` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `title` TEXT NOT NULL, `content` TEXT NOT NULL, `uuid` TEXT NOT NULL, `mtime` INTEGER, `icon` TEXT NOT NULL DEFAULT 'R.drawable.ic_notes_hint_24dp', `coverImageUrl` TEXT, `is_dummy` INTEGER NOT NULL, `type` INTEGER NOT NULL, `subType` INTEGER NOT NULL, `createTime` INTEGER NOT NULL, `updateTime` INTEGER NOT NULL, `finishTime` INTEGER NOT NULL, `deleteTime` INTEGER NOT NULL, `archiveTime` INTEGER NOT NULL, `pinTime` INTEGER NOT NULL, `lockTime` INTEGER NOT NULL, `blockTime` INTEGER NOT NULL, `startTime` INTEGER NOT NULL, `totalLength` INTEGER NOT NULL, `label` INTEGER NOT NULL, `status` INTEGER NOT NULL, `theme` INTEGER NOT NULL, `color` INTEGER NOT NULL, `miniShowType` INTEGER NOT NULL, `render_type` INTEGER NOT NULL, `order` INTEGER NOT NULL, `tags` TEXT NOT NULL, `topics` TEXT NOT NULL, `parent` TEXT NOT NULL, `ext_ext` TEXT NOT NULL, `attachmentItems_attachmentItems` TEXT NOT NULL)"
                    )
                    database.execSQL("INSERT INTO $newTable SELECT `id`, `name`, `title`, `content`, `uuid`, `mtime`, `icon`, `coverImageUrl`, `is_dummy`, `type`, `subType`, `createTime`, `updateTime`, `finishTime`, `deleteTime`, `archiveTime`, `pinTime`, `lockTime`, `blockTime`, `startTime`, `totalLength`, `label`, `status`, `theme`, `color`, `miniShowType`, `render_type`, `order`, `tags`, `topics`, `parent`, `ext_ext`, `attachmentItems_attachmentItems` FROM $oldTable")
                    database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_${newTable}_uuid` ON `${newTable}` (`uuid`)")
                }

                database.setTransactionSuccessful()
                database.endTransaction()
            }
        }

        @JvmField
        val MIGRATION_9_10: Migration = object : Migration(9, 10) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.beginTransaction()

                var VIEW_NAME = "view_markdown"
                database.execSQL("DROP VIEW `${VIEW_NAME}`")
                VIEW_NAME = "view_page"
                database.execSQL("DROP VIEW `${VIEW_NAME}`")
                VIEW_NAME = "view_thing"
                database.execSQL("DROP VIEW `${VIEW_NAME}`")

                //Habit
                val habitQuery = database.query("SELECT * FROM Habit")
                while (habitQuery.moveToNext()) {
                    val recordId = habitQuery.getLong(0)
                    val recordQuery = database.query("SELECT ext_ext FROM records WHERE id = ${recordId} LIMIT 1")
                    if (recordQuery.count <= 0) {
                        recordQuery.close()
                        continue
                    }
                    val ext = recordQuery.getString(0)
                    LogUtils.debugInfo(ext)
                    recordQuery.close()

                    val habit = Habit(
                        recordId,
                        habitQuery.getInt(1),
                        habitQuery.getInt(2),
                        habitQuery.getString(3),
                        habitQuery.getString(4),
                        habitQuery.getString(5),
                        habitQuery.getLong(6),
                        habitQuery.getLong(7)
                    )
                    val habitReminderQuery = database.query("SELECT * FROM HabitReminder WHERE habitId = ${recordId}")
                    val habitReminders: MutableList<HabitReminder> = mutableListOf()
                    while (habitReminderQuery.moveToNext()) {
                        val hr = HabitReminder(
                            habitReminderQuery.getLong(0),
                            habitReminderQuery.getLong(1),
                            habitReminderQuery.getLong(2)
                        )
                        habitReminders.add(hr)
                    }
                    habitReminderQuery.close()
                    habit.habitReminders = habitReminders

                    val habitRecordQuery = database.query("SELECT * FROM HabitRecord WHERE habitId = ${recordId}")
                    val habitRecords: MutableList<HabitRecord> = mutableListOf()
                    while (habitRecordQuery.moveToNext()) {
                        val hr = HabitRecord(
                            habitRecordQuery.getLong(0),
                            habitRecordQuery.getLong(1),
                            habitRecordQuery.getLong(2),
                            habitRecordQuery.getLong(3),
                            habitRecordQuery.getInt(4),
                            habitRecordQuery.getInt(5),
                            habitRecordQuery.getInt(6),
                            habitRecordQuery.getInt(7),
                            habitRecordQuery.getInt(8)
                        )
                        habitRecords.add(hr)
                    }
                    habitRecordQuery.close()
                    habit.habitRecords = habitRecords

                    val json = JSON.parseObject(ext)
                    json.put("habit", habit.toJsonObject())
                    val ext_str = json.toJSONString()
                    // update
                    database.execSQL("UPDATE records SET ext_ext = ${ext_str} WHERE id = ${recordId}")
                }
                var TABLE_NAME = "Habit" //将表里的数据迁移到item的json里
                database.execSQL("DROP TABLE `${TABLE_NAME}`")
                TABLE_NAME = "HabitReminder"
                database.execSQL("DROP TABLE `${TABLE_NAME}`")
                TABLE_NAME = "HabitRecord"
                database.execSQL("DROP TABLE `${TABLE_NAME}`")

                //reminder
                val reminderQuery = database.query("SELECT * FROM Reminder")
                while (reminderQuery.moveToNext()) {
                    val recordId = reminderQuery.getLong(0)
                    val recordQuery = database.query("SELECT ext_ext FROM records WHERE id = ${recordId} LIMIT 1")
                    if (recordQuery.count <= 0) {
                        recordQuery.close()
                        continue
                    }
                    val ext = recordQuery.getString(0)
                    LogUtils.debugInfo(ext)
                    recordQuery.close()

                    val reminder = Reminder(
                        recordId,
                        reminderQuery.getLong(1),
                        reminderQuery.getInt(2),
                        reminderQuery.getLong(3),
                        reminderQuery.getLong(4),
                        reminderQuery.getLong(5)
                    )

                    val json = JSON.parseObject(ext)
                    json.put("reminder", reminder.toJsonObject())
                    val ext_str = json.toJSONString()
                    // update
                    database.execSQL("UPDATE records SET ext_ext = ${ext_str} WHERE id = ${recordId}")
                }
                TABLE_NAME = "Reminder"
                database.execSQL("DROP TABLE `${TABLE_NAME}`")

                //DoingRecord
                val doingRecordQuery = database.query("SELECT * FROM DoingRecord")
                while (doingRecordQuery.moveToNext()) {
                    val recordId = doingRecordQuery.getLong(1)  // thingId
                    val recordQuery = database.query("SELECT ext_ext FROM records WHERE id = ${recordId} LIMIT 1")
                    if (recordQuery.count <= 0) {
                        recordQuery.close()
                        continue
                    }
                    val ext = recordQuery.getString(0)
                    LogUtils.debugInfo(ext)
                    recordQuery.close()

                    val doingRecord = DoingRecord(
                        doingRecordQuery.getLong(0),
                        recordId,
                        doingRecordQuery.getInt(2),
                        doingRecordQuery.getInt(3),
                        doingRecordQuery.getInt(4),
                        doingRecordQuery.getLong(5),
                        doingRecordQuery.getLong(6),
                        doingRecordQuery.getLong(7),
                        doingRecordQuery.getLong(8),
                        doingRecordQuery.getInt(9),
                        doingRecordQuery.getInt(10),
                        doingRecordQuery.getInt(11) != 0
                    )

                    val jsonObject = JSON.parseObject(ext)
                    val data = jsonObject.getJSONArray("doingRecords").map {
                        val json = it as? JSONObject
                        json?.let {
                            DoingRecord.fromJson(it.toJSONString())
                        }
                    }.filterNotNull().toMutableList()
                    data.add(doingRecord)
                    jsonObject.put("doingRecords", data.map { it.toJsonObject() })
                    val ext_str = jsonObject.toJSONString()
                    // update
                    database.execSQL("UPDATE records SET ext_ext = ${ext_str} WHERE id = ${recordId}")
                }
                TABLE_NAME = "DoingRecord"
                database.execSQL("DROP TABLE `${TABLE_NAME}`")
                TABLE_NAME = "RoomRepetition"
                database.execSQL("DROP TABLE `${TABLE_NAME}`")

                database.setTransactionSuccessful()
                database.endTransaction()
            }
        }
    }
}