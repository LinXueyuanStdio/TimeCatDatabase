package com.timecat.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import com.timecat.data.room.record.RoomRecord
import com.timecat.data.room.tag.Tag

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/6/6
 * @description null
 * @usage null
 */
@Dao
interface TransDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replaceRoomRecord(data: Collection<RoomRecord>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replaceTag(data: Collection<Tag>)

    @Transaction
    fun importFromFile(
        notes: Collection<RoomRecord>,
        tags: Collection<Tag>,
    ) {
        replaceRoomRecord(notes)
        replaceTag(tags)
    }
}