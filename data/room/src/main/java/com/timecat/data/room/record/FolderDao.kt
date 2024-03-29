package com.timecat.data.room.record

import androidx.room.*
import com.timecat.identity.data.block.type.BLOCK_CONTAINER

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/4/11
 * @description null
 * @usage null
 */
@Dao
interface FolderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFolder(note: RoomRecord): Long

    @Delete
    fun delete(note: RoomRecord?)

    @Query("SELECT count(*) FROM records WHERE type = $BLOCK_CONTAINER")
    fun getCount(): Int

    @Query("SELECT * FROM records WHERE type = $BLOCK_CONTAINER ORDER BY createTime DESC")
    fun getAll(): List<RoomRecord>

    @Query("SELECT * FROM records WHERE type = $BLOCK_CONTAINER AND id = :uid LIMIT 1")
    fun getByID(uid: Int): RoomRecord?

    @Query("SELECT * FROM records WHERE type = $BLOCK_CONTAINER AND uuid = :uuid LIMIT 1")
    fun getByUUID(uuid: String): RoomRecord?

}