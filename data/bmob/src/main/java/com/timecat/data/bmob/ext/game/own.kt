package com.timecat.data.bmob.ext.game

import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.data.game.*
import com.timecat.data.bmob.data.mail.OwnMail
import org.joda.time.DateTime

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

fun ownActivity(
    user: User,
    block: Block,
    activeDateTime: DateTime,
    expireDateTime: DateTime
) = OwnActivity().apply {
    this.user = user
    this.activity = block
    this.activeDateTime = activeDateTime
    this.expireDateTime = expireDateTime
}

fun ownTask(user: User, activity: Block, task: Block) = OwnTask().apply {
    this.user = user
    this.task = task
    this.activity = activity
}

fun pay(user: User, shop: Block, pay: Int, money: Block, gain: Int, good: Block) = Pay().apply {
    this.user = user
    this.shop = shop
    this.pay = pay
    this.money = money
    this.gain = gain
    this.good = good
}

fun pay(user: User, shop: Block) = Pay().apply {
    this.user = user
    this.shop = shop
}
