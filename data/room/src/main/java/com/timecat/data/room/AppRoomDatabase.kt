package com.timecat.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.timecat.data.room.record.JsonTypeConverter
import com.timecat.data.room.space.Space
import com.timecat.data.room.space.SpaceDao
import com.timecat.data.room.widget.Widget
import com.timecat.data.room.widget.WidgetDao

/**
 * 时光猫超空间
 */
@Database(
    entities = [
        Space::class,
        Widget::class
    ],
    version = AppRoomDatabase.EXPORT_VERSION,
    exportSchema = true
)
@TypeConverters(TypeConverter::class, JsonTypeConverter::class)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun widgetDao(): WidgetDao
    abstract fun spaceDao(): SpaceDao

    companion object {
        const val NAME = "timecat_app_room.db"
        const val EXPORT_VERSION = 1

        @JvmStatic
        fun forMemory(context: Context): AppRoomDatabase {
            return Room.inMemoryDatabaseBuilder(
                context.applicationContext,
                AppRoomDatabase::class.java
            )
                .allowMainThreadQueries()
                .build()
        }

        @JvmStatic
        var instance: AppRoomDatabase? = null

        @JvmStatic
        @JvmOverloads
        fun forFile(context: Context, fileName: String = NAME): AppRoomDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppRoomDatabase::class.java,
                    fileName
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance!!
        }
    }
}