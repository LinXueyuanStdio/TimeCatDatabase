package com.timecat.data.bmob.ext.net

import cn.leancloud.AVQuery
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.game.agent.OwnCube
import com.timecat.data.bmob.data.game.item.OwnItem
import com.timecat.data.bmob.data.mail.OwnMail

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/18
 * @description null
 * @usage null
 */
fun User.allOwnCube() = AVQuery<OwnCube>("OwnCube").apply {
    whereEqualTo("user", this)
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

fun User.allOwnItem() = AVQuery<OwnItem>("OwnItem").apply {
    whereEqualTo("user", this)
    order("-createdAt")
    include("user")
    include("item")
}

fun User.allOwnMail(): AVQuery<OwnMail> {
    val q = AVQuery<OwnMail>("OwnMail")
    q.whereEqualTo("user", this)
    q.include("user")
    q.include("block")
    q.order("-createdAt")
    return q
}