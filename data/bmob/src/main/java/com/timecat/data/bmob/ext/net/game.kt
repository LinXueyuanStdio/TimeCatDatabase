package com.timecat.data.bmob.ext.net

import cn.bmob.v3.BmobQuery
import com.timecat.data.bmob.data._User
import com.timecat.data.bmob.data.game.agent.OwnCube
import com.timecat.data.bmob.data.game.item.OwnItem

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/18
 * @description null
 * @usage null
 */
fun _User.allOwnCube() = BmobQuery<OwnCube>().apply {
    addWhereEqualTo("user", this)
    order("-createdAt")
    include("user," +
        "cube," +
        "equipment_1," +
        "equipment_2," +
        "equipment_3," +
        "equipment_4," +
        "equipment_5," +
        "equipment_6")
}

fun _User.allOwnItem() = BmobQuery<OwnItem>().apply {
    addWhereEqualTo("user", this)
    order("-createdAt")
    include("user,item")
}