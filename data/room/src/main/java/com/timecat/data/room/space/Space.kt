package com.timecat.data.room.space

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.timecat.component.setting.FILE
import com.timecat.component.setting.RepoSchema
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/4/4
 * @description null
 * @usage null
 */

@Entity(
    tableName = "Space",

    indices = [
        Index("uuid", unique = true)
    ]
)
data class Space(
    @PrimaryKey(autoGenerate = true)
    var id: Long,

    var title: String = "",
    var content: String = "",
    var url: String = "", //root dir of space
    var uuid: String = UUID.randomUUID().toString(),

    @ColumnInfo(defaultValue = "R.drawable.ic_notes_hint_24dp")
    var icon: String = "R.drawable.ic_notes_hint_24dp",
    var coverImageUrl: String? = "R.drawable.ic_notes_hint_24dp",
    var order: Long = 0,
) {
    val dbPath: String
        get() {
            if (url == FILE.RoomDatabase.fileName) {
                return url
            }
            return RepoSchema.rootDb(url).absolutePath
        }

    companion object {
        fun default(): Space = Space(
            0,
            "时光猫",
            "时光猫本源",
            FILE.RoomDatabase.fileName,
            "时光猫",
            "R.drawable.ic_launcher",
            "R.drawable.ic_launcher"
        )
    }
}