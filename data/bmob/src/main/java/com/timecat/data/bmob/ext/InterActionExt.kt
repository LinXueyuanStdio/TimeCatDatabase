package com.timecat.data.bmob.ext

import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.data.common.InterAction
import com.timecat.identity.data.action.INTERACTION_Auth_Identity
import com.timecat.identity.data.action.INTERACTION_Auth_Permission
import com.timecat.identity.data.action.INTERACTION_Auth_Role
import com.timecat.identity.data.action.InterActionType

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/11/7
 * @description null
 * @usage null
 */
fun interaction(
    author: User,
    source: Block,
    target: User,
    @InterActionType type: Int
) = InterAction(author, source, target, type)

infix fun User.auth_Identity(p: Pair<Block, User>): InterAction {
    return interaction(this, p.first, p.second, INTERACTION_Auth_Identity)
}

infix fun User.auth_Role(p: Pair<Block, User>): InterAction {
    return interaction(this, p.first, p.second, INTERACTION_Auth_Role)
}

infix fun User.auth_Permission(p: Pair<Block, User>): InterAction {
    return interaction(this, p.first, p.second, INTERACTION_Auth_Permission)
}