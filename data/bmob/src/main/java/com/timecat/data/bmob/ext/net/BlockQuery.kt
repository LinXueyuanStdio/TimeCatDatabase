package com.timecat.data.bmob.ext.net

import cn.leancloud.AVQuery
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.data.block.*
import com.timecat.identity.data.block.type.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description null
 * @usage null
 */
//region all block (by  type)
//moment
fun allMoment() = allBlockByType(BLOCK_MOMENT)

//forum
fun allForum() = allBlockByType(BLOCK_FORUM)

//topic
fun allTopic() = allBlockByType(BLOCK_TOPIC)

//tag
fun allTag() = allBlockByType(BLOCK_TAG)

//role
fun allRole() = allBlockByType(BLOCK_ROLE)

//identity
fun allIdentity() = allBlockByType(BLOCK_IDENTITY)

//skin
fun allSkin() = allBlockByType(BLOCK_SKIN)

//app
fun allApp() = allBlockByType(BLOCK_APP)
fun allAndroidApp() = allApp().apply {
    whereEqualTo("subtype", APP_AndroidApp)
}

fun alliOSApp() = allApp().apply {
    whereEqualTo("subtype", APP_iOS)
}

fun allWindowApp() = allApp().apply {
    whereEqualTo("subtype", APP_Windows)
}

fun allLinuxApp() = allApp().apply {
    whereEqualTo("subtype", APP_Linux)
}

fun allMacApp() = allApp().apply {
    whereEqualTo("subtype", APP_Mac)
}

fun allWebApp() = allApp().apply {
    whereEqualTo("subtype", APP_WebApp)
}

fun allPluginApp() = allApp().apply {
    whereEqualTo("subtype", APP_Plugin)
}

//cube
fun allCube() = allIdentity()

//message
fun allMessage() = allBlockByType(BLOCK_MESSAGE)

//mail
fun allMail() = allBlockByType(BLOCK_MAIL)

//shop
fun allShop() = allBlockByType(BLOCK_SHOP)

//item
fun allItem() = allBlockByType(BLOCK_ITEM)
fun allThingItem() = allItem().apply {
    whereEqualTo("subtype", ITEM_Thing)
}

fun allPackageItem() = allItem().apply {
    whereEqualTo("subtype", ITEM_Package)
}

fun allDataItem() = allItem().apply {
    whereEqualTo("subtype", ITEM_Data)
}

fun allEquipItem() = allItem().apply {
    whereEqualTo("subtype", ITEM_Equip)
}

fun allBuffItem() = allItem().apply {
    whereEqualTo("subtype", ITEM_Buff)
}

//activity
fun allActivity() = allBlockByType(BLOCK_ACTIVITY)

//task
fun allTask() = allBlockByType(BLOCK_TASK)

//leader board
fun allLeaderBoard() = allBlockByType(BLOCK_LEADER_BOARD)

//permission
fun allPermission() = allBlockByType(BLOCK_PERMISSION)
fun allMetaPermission() = allPermission().apply {
    whereEqualTo("subtype", PERMISSION_Meta)
}

fun allHunPermission() = allPermission().apply {
    whereEqualTo("subtype", PERMISSION_Hun)
}

fun allBlockByType(type: Int) = AVQuery<Block>("Block").apply {
    whereEqualTo("type", type)
    include("user")
    include("parent")
    order("-createdAt")
    setLimit(200)
}

fun allBlock() = AVQuery<Block>("Block").apply {
    include("user")
    include("parent")
    order("-createdAt")
    setLimit(200)
}

fun allBlockByIds(ids: List<String>) = AVQuery<Block>("Block").apply {
    include("user")
    include("parent")
    whereContainedIn("objectId", ids)
    order("-createdAt")
    setLimit(200)
}
//endregion

//region all block (by  type) for user
fun User.allMoment() = allBlockByType(BLOCK_MOMENT)
fun User.allForum() = allBlockByType(BLOCK_FORUM)
fun User.allTopic() = allBlockByType(BLOCK_TOPIC)
fun User.allTag() = allBlockByType(BLOCK_TAG)
fun User.allRole() = allBlockByType(BLOCK_ROLE)
fun User.allIdentity() = allBlockByType(BLOCK_IDENTITY)
fun User.allPermission() = allBlockByType(BLOCK_PERMISSION)
fun User.allMetaPermission() = allPermission().apply {
    whereEqualTo("subtype", PERMISSION_Meta)
}

fun User.allHunPermission() = allPermission().apply {
    whereEqualTo("subtype", PERMISSION_Hun)
}

fun User.allBlockByType(type: Int) = AVQuery<Block>("Block").apply {
    whereEqualTo("user", this@allBlockByType)
    whereEqualTo("type", type)
    include("user")
    include("parent")
    order("-createdAt")
    setLimit(200)
}

fun User.allBlock() = AVQuery<Block>("Block").apply {
    include("user")
    include("parent")
    order("-createdAt")
    setLimit(200)
}
//endregion

fun oneBlockOf(id: String) = AVQuery<Block>("Block").apply {
    whereEqualTo("objectId", id)
    order("-createdAt")
    include("user")
    include("parent")
    setLimit(1)
}

//region children of block
fun childrenOf(block: Block, type: List<Int>) = AVQuery<Block>("Block").apply {
    whereEqualTo("parent", block)
    order("-createdAt")
    include("user")
    include("parent")
    whereContainedIn("type", type)
}

fun childrenOf(block: Block, type: Int) = AVQuery<Block>("Block").apply {
    whereEqualTo("parent", block)
    order("-createdAt")
    include("user")
    include("parent")
    whereEqualTo("type", type)
}

fun childrenOf(blocks: List<Block>, type: List<Int>) = AVQuery<Block>("Block").apply {
    whereContainedIn("parent", blocks)
    order("-createdAt")
    include("user")
    include("parent")
    whereContainedIn("type", type)
}

fun Block.findAllPost() = childrenOf(this, BLOCK_POST)
fun Block.findAllMoment() = childrenOf(this, BLOCK_MOMENT)
fun Block.findAllComment() = childrenOf(this, BLOCK_COMMENT)
fun Block.findAllSubComment() = AVQuery<Block>("Block").apply {
    whereStartsWith("title", "$title/$objectId")
    whereEqualTo("parent", parent)
    whereEqualTo("space", space)
    whereEqualTo("type", type)
    order("-createdAt")
    include("user")
    include("parent")
}

fun Block.findAllBlock() = AVQuery<Block>("Block").apply {
    whereEqualTo("parent", this@findAllBlock)
    order("-updatedAt")
    setLimit(200)
}
//endregion

fun checkBlockExistByTitle(title: String, type: Int): AVQuery<Block> = AVQuery<Block>("Block").apply {
    whereEqualTo("title", title)
    order("-createdAt")
    whereEqualTo("type", type)
}

fun checkTagExistByTitle(title: String) = checkBlockExistByTitle(title, BLOCK_TAG)
fun checkTopicExistByTitle(title: String) = checkBlockExistByTitle(title, BLOCK_TOPIC)
fun checkForumExistByTitle(title: String) = checkBlockExistByTitle(title, BLOCK_FORUM)
fun checkLeaderBoardExistByTitle(title: String) = checkBlockExistByTitle(title, BLOCK_LEADER_BOARD)
fun checkAppExistByTitle(title: String) = checkBlockExistByTitle(title, BLOCK_APP)
fun checkItemExistByTitle(title: String) = checkBlockExistByTitle(title, BLOCK_ITEM)
fun checkPermissionExistByTitle(title: String, subtype: Int) = checkBlockExistByTitle(title, BLOCK_PERMISSION).apply {
    whereEqualTo("subtype", subtype)
}

fun checkMetaPermExistByTitle(title: String) = checkPermissionExistByTitle(title, PERMISSION_Meta)
fun checkHunPermExistByTitle(title: String) = checkPermissionExistByTitle(title, PERMISSION_Hun)
fun checkRoleExistByTitle(title: String) = checkBlockExistByTitle(title, BLOCK_ROLE)
fun checkIdentityExistByTitle(title: String) = checkBlockExistByTitle(title, BLOCK_IDENTITY)
