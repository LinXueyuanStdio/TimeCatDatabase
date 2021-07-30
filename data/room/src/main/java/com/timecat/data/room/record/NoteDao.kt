package com.timecat.data.room.record

import androidx.room.*
import com.timecat.identity.data.base.TASK_DELETE
import com.timecat.identity.data.base.TASK_PIN
import com.timecat.identity.data.block.type.BLOCK_MARKDOWN

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/4/11
 * @description null
 * @usage null
 */
@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: RoomRecord): Long

    @Delete
    fun delete(note: RoomRecord?)

    @Query("SELECT count(*) FROM records WHERE type = $BLOCK_MARKDOWN")
    fun getCount(): Int

    @Query("SELECT * FROM records WHERE type = $BLOCK_MARKDOWN ORDER BY (status & $TASK_PIN) DESC, createTime DESC")
    fun getAll(): List<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_MARKDOWN AND (status & $TASK_DELETE <> 0) AND updateTime < :timestamp")
    fun getOldTrashedNotes(timestamp: Long): List<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_MARKDOWN AND id = :uid LIMIT 1")
    fun getByID(uid: Int): RoomRecord?

    @Query("SELECT * FROM records WHERE type = $BLOCK_MARKDOWN AND uuid = :uuid LIMIT 1")
    fun getByUUID(uuid: String): RoomRecord?

}