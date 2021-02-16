package com.timecat.data.bmob.ext.game

import cn.leancloud.AVObject
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.data.game.OwnCube
import com.timecat.data.bmob.data.game.OwnItem
import com.timecat.data.bmob.data.mail.OwnMail

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/15
 * @description null
 * @usage null
 */

fun ownMail(user: User, mail: Block) = OwnMail().apply {
    this.user = user
    this.mail = mail
}

fun ownCube(user: User, cube: Block) = OwnCube.simple(user, cube)

fun ownItem(user: User, item: Block, count: Int) = OwnItem().apply {
    this.user = user
    this.item = item
    this.count = count
}