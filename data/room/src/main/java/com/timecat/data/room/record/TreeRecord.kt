package com.timecat.data.room.record

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/19
 * @description null
 * @usage null
 */
data class TreeRecord(
    val node: RoomRecord,
    val children: MutableList<TreeRecord>) {
}