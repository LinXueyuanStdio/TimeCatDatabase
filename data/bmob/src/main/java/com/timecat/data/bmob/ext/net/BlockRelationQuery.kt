package com.timecat.data.bmob.ext.net

import cn.leancloud.AVQuery
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.data.common.Block2Block
import com.timecat.identity.data.block_block.Block2Block_Identity_has_role
import com.timecat.identity.data.block_block.Block2Block_Leaderboard_has_block
import com.timecat.identity.data.block_block.Block2Block_Role_has_permission

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/7
 * @description null
 * @usage null
 */
fun children(blocks: List<Block>, type: List<Int>) = AVQuery<Block2Block>("Block2Block").apply {
    whereContainedIn("from", blocks.map { it.objectId })
    order("-createdAt")
    include("user,from.user,to.user")
    whereContainedIn("type", type)
}
typealias Roles = List<Block>

fun Roles.findAllPermission() = children(this, listOf(Block2Block_Role_has_permission))
fun Roles.findAllMetaPermission() = findAllPermission().apply {
    whereMatchesQuery("to", allMetaPermission())
}

fun Roles.findAllHunPermission() = findAllPermission().apply {
    whereMatchesQuery("to", allHunPermission())
}
typealias Identities = List<Block>

fun Identities.findAllRoles() = children(this, listOf(Block2Block_Identity_has_role))


fun children(block: Block, type: List<Int>) = AVQuery<Block2Block>("Block2Block").apply {
    whereEqualTo("from", block)
    order("-createdAt")
    include("user,from.user,to.user")
    whereContainedIn("type", type)
}

fun Block.findAllRole() = children(this, listOf(Block2Block_Identity_has_role))
fun Block.findAllPermission() = children(this, listOf(Block2Block_Role_has_permission))
fun Block.findAllMetaPermission() = findAllPermission().apply {
    whereMatchesQuery("to", allMetaPermission())
}

fun Block.findAllHunPermission() = findAllPermission().apply {
    whereMatchesQuery("to", allHunPermission())
}

fun Block.findAllItemsInLeaderBoard() = children(this, listOf(Block2Block_Leaderboard_has_block))