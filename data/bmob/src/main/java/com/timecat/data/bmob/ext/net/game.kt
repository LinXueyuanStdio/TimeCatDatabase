package com.timecat.data.bmob.ext.net

import cn.leancloud.AVQuery
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.game.OwnActivity
import com.timecat.data.bmob.data.game.OwnCube
import com.timecat.data.bmob.data.game.OwnItem
import com.timecat.data.bmob.data.game.OwnTask
import com.timecat.data.bmob.data.mail.OwnMail

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/18
 * @description null
 * @usage null
 */
fun User.allOwnCube() = AVQuery<OwnCube>("OwnCube").apply {
    whereEqualTo("user", this@allOwnCube)
    order("-createdAt")
    include("user")
    include("cube")
    include("equipment_1")
    include("equipment_2")
    include("equipment_3")
    include("equipment_4")
    include("equipment_5")
    include("equipment_6")
}

fun User.allOwnItem() = AVQuery<OwnItem>("OwnItem").apply {
    whereEqualTo("user", this@allOwnItem)
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

fun User.allOwnActivity(): AVQuery<OwnActivity> {//TODO
    val q = AVQuery<OwnActivity>("OwnActivity")
    q.whereEqualTo("user", this)
    q.include("user")
    q.include("block")
    q.order("-createdAt")
    return q
}

fun User.allOwnTask(): AVQuery<OwnTask> {//TODO
    val q = AVQuery<OwnTask>("OwnTask")
    q.whereEqualTo("user", this)
    q.include("user")
    q.include("block")
    q.order("-createdAt")
    return q
}
