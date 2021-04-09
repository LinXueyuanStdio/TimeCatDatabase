package com.timecat.data.room

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.timecat.component.setting.FILE
import com.timecat.data.room.doing.DoingDao
import com.timecat.data.room.doing.DoingRecord
import com.timecat.data.room.habit.*
import com.timecat.data.room.record.*
import com.timecat.data.room.reminder.Reminder
import com.timecat.data.room.reminder.ReminderDao
import com.timecat.data.room.tag.Tag
import com.timecat.data.room.tag.TagDao
import com.timecat.data.room.views.*

/**
 * 时光猫超空间
 */
@Database(
    entities = [
        RoomRecord::class,//export
        DoingRecord::class,//export
        Reminder::class,//export
        Tag::class,//export
        RoomRepetition::class,//export
        Habit::class,//export
        HabitRecord::class,//export
        HabitReminder::class,//export
//        RoomUser::class,
//        EventType::class,
//        ImExport::class,
//        ImportRecord::class,
//        Widget::class,
//        NoteWidget::class
    ],
    views = [
//        Book::class,
//        Dialog::class,
//        Event::class,
        Markdown::class,
        Page::class,
        Thing::class
    ],
    version = TimeCatRoomDatabase.EXPORT_VERSION,
    exportSchema = true
)
@TypeConverters(TypeConverter::class, JsonTypeConverter::class)
abstract class TimeCatRoomDatabase : RoomDatabase() {
    abstract fun recordDao(): RecordDao
    abstract fun tagDao(): TagDao
    abstract fun repetitionDao(): RepetitionDao
    abstract fun doingDao(): DoingDao
    abstract fun reminderDao(): ReminderDao
    abstract fun habitDao(): HabitDao
    abstract fun habitRecordDao(): HabitRecordDao
    abstract fun habitReminderDao(): HabitReminderDao

    abstract fun noteDao(): NoteDao
    abstract fun folderDao(): FolderDao
    abstract fun transDao(): TransDao

    //view
    abstract fun MarkdownDao(): MarkdownDao
    abstract fun PageDao(): PageDao
    abstract fun ThingDao(): ThingDao


    companion object {
        const val NAME = "timecat_room.db"
        const val EXPORT_VERSION = 9

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
                    )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            AsyncTask.execute {
                                forFile(context).recordDao().initConversation()
                            }
                        }

                        override fun onOpen(db: SupportSQLiteDatabase) {
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
                    database.execSQL("CREATE TABLE IF NOT EXISTS `${newTable}` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `title` TEXT NOT NULL, `content` TEXT NOT NULL, `uuid` TEXT NOT NULL, `mtime` INTEGER, `icon` TEXT NOT NULL DEFAULT 'R.drawable.ic_notes_hint_24dp', `coverImageUrl` TEXT, `is_dummy` INTEGER NOT NULL, `type` INTEGER NOT NULL, `subType` INTEGER NOT NULL, `createTime` INTEGER NOT NULL, `updateTime` INTEGER NOT NULL, `finishTime` INTEGER NOT NULL, `deleteTime` INTEGER NOT NULL, `archiveTime` INTEGER NOT NULL, `pinTime` INTEGER NOT NULL, `lockTime` INTEGER NOT NULL, `blockTime` INTEGER NOT NULL, `startTime` INTEGER NOT NULL, `totalLength` INTEGER NOT NULL, `label` INTEGER NOT NULL, `status` INTEGER NOT NULL, `theme` INTEGER NOT NULL, `color` INTEGER NOT NULL, `miniShowType` INTEGER NOT NULL, `render_type` INTEGER NOT NULL, `order` INTEGER NOT NULL, `tags` TEXT NOT NULL, `topics` TEXT NOT NULL, `parent` TEXT NOT NULL, `ext_ext` TEXT NOT NULL, `attachmentItems_attachmentItems` TEXT NOT NULL)")
                    database.execSQL("INSERT INTO $newTable SELECT `id`, `name`, `title`, `content`, `uuid`, `mtime`, `icon`, `coverImageUrl`, `is_dummy`, `type`, `subType`, `createTime`, `updateTime`, `finishTime`, `deleteTime`, `archiveTime`, `pinTime`, `lockTime`, `blockTime`, `startTime`, `totalLength`, `label`, `status`, `theme`, `color`, `miniShowType`, `render_type`, `order`, `tags`, `topics`, `parent`, `ext_ext`, `attachmentItems_attachmentItems` FROM $oldTable")
                    database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_${newTable}_uuid` ON `${newTable}` (`uuid`)")
                }

                database.setTransactionSuccessful()
                database.endTransaction()
            }
        }
    }
}