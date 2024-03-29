package com.timecat.data.bmob.ext

import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.data.common.Block2Block
import com.timecat.identity.data.block_block.Block2BlockType
import com.timecat.identity.data.block_block.Block2Block_Identity_has_role
import com.timecat.identity.data.block_block.Block2Block_Role_has_permission

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/11/4
 * @description null
 * @usage null
 */
fun relation(
    author: User,
    source: Block,
    target: Block,
    @Block2BlockType type: Int
) = Block2Block().apply {
    this.user = author
    this.from = source
    this.to = target
    this.type = type
    this.structure = ""
    this.status = 0
}

infix fun User.let_Identity_has_role(identity_to_role: Pair<Block, Block>): Block2Block {
    return relation(this, identity_to_role.first, identity_to_role.second, Block2Block_Identity_has_role)
}

infix fun User.let_Role_has_permission(role_to_permission: Pair<Block, Block>): Block2Block {
    return relation(this, role_to_permission.first, role_to_permission.second, Block2Block_Role_has_permission)
}
