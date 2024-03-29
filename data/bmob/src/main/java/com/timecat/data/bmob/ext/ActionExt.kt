package com.timecat.data.bmob.ext

import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Action
import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.data.action.ACTION_FOCUS
import com.timecat.identity.data.action.ACTION_LIKE
import com.timecat.identity.data.action.ACTION_RECOMMEND

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-16
 * @description null
 * @usage null
 */
fun action(user: User, block: Block, type: Int) = Action().apply {
    this.user = user
    this.block = block
    this.type = type
    this.structure = ""
    this.status = 0
}

infix fun User.follow(target: Block): Action = action(this, target, ACTION_FOCUS)
infix fun User.like(target: Block): Action = action(this, target, ACTION_LIKE)
infix fun User.recommend(target: Block): Action = action(this, target, ACTION_RECOMMEND)
