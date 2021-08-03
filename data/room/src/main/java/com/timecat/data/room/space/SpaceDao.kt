package com.timecat.data.room.space

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.timecat.data.room.BaseDao

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/4/4
 * @description null
 * @usage null
 */
@Dao
abstract class SpaceDao : BaseDao<Space> {

    @Query("SELECT * FROM Space WHERE id = :id LIMIT 1")
    abstract fun get(id: Long): Space?

    @Query("SELECT * FROM Space WHERE title = :title LIMIT 1")
    abstract fun get(title: String): Space?

    @Query("SELECT * FROM Space WHERE uuid = :uuid")
    abstract fun getByUuid(uuid: String): Space?

    @Query("SELECT * FROM Space ORDER BY `order` DESC")
    abstract fun getAll(): MutableList<Space>

    @Query("SELECT * FROM Space ORDER BY `order` DESC LIMIT :pageSize OFFSET :offset")
    abstract fun getAll(pageSize: Int, offset: Int): MutableList<Space>

    @Transaction
    open fun listSpaces(): MutableList<Space> {
        val list = mutableListOf(Space.default())
        list.addAll(getAll())
        return list
    }

    @Query("SELECT count(*) FROM Space")
    abstract fun count(): Int
}