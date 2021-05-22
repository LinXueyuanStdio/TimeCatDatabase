package com.timecat.data.bmob.ext

import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.data.block.*
import com.timecat.identity.data.block.type.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description null
 * @usage null
 */
//region 创建一个次级块，和父块相关
class UserBlockPack(
    var user: User,
    var block: Block
) {
    fun build(b: BlockBuilder): Block {
        return Block.forName(user, b.type).apply {
            this.subtype = b.subtype
            this.parent = block
            this.title = b.title
            this.content = b.content
            this.className = b.tableName
        }
    }
}

infix fun User.commentsOn(parent: Block) = UserBlockPack(this, parent)
infix fun UserBlockPack.with(builder: CommentBuilder): Block {
    return build(builder)
}

infix fun User.postsOn(parent: Block) = UserBlockPack(this, parent)
infix fun UserBlockPack.with(builder: PostBuilder): Block {
    return build(builder)
}
//endregion

//region 创建一个顶级块，和其他块无关
infix fun User.createBlock(builder: BlockBuilder): Block {
    return Block.forName(this, builder.type).apply {
        this.subtype = builder.subtype
        this.title = builder.title
        this.content = builder.content
        this.className = builder.tableName
        this.parent = builder.parent
    }
}

infix fun User.create(builder: ForumBuilder): Block {
    return createBlock(builder).apply {
        structure = builder.headerBlock.toJson()
    }
}

infix fun User.create(builder: LeaderBoardBuilder): Block {
    return createBlock(builder).apply {
        structure = builder.headerBlock.toJson()
    }
}

infix fun User.create(builder: AppBuilder): Block {
    return createBlock(builder).apply {
        structure = builder.headerBlock.toJson()
    }
}

infix fun User.create(builder: MailBuilder): Block {
    return createBlock(builder).apply {
        structure = builder.headerBlock.toJson()
    }
}

infix fun User.create(builder: ShopBuilder): Block {
    return createBlock(builder).apply {
        structure = builder.headerBlock.toJson()
    }
}

infix fun User.create(builder: ItemBuilder): Block {
    return createBlock(builder).apply {
        structure = builder.headerBlock.toJson()
    }
}

infix fun User.create(builder: ActivityBuilder): Block {
    return createBlock(builder).apply {
        structure = builder.headerBlock.toJson()
    }
}

infix fun User.create(builder: TaskBuilder): Block {
    return createBlock(builder).apply {
        structure = builder.headerBlock.toJson()
    }
}

infix fun User.create(builder: TopicBuilder): Block {
    return createBlock(builder).apply {
        structure = builder.headerBlock.toJson()
    }
}

infix fun User.create(builder: TagBuilder): Block {
    return createBlock(builder).apply {
        structure = builder.headerBlock.toJson()
    }
}

infix fun User.create(builder: PermissionBuilder): Block {
    return createBlock(builder)
}

infix fun User.create(builder: RoleBuilder): Block {
    return createBlock(builder)
}

infix fun User.create(builder: IdentityBuilder): Block {
    return createBlock(builder)
}

infix fun User.create(builder: CommentBuilder): Block {
    return createBlock(builder).apply {
        structure = builder.headerBlock.toJson()
    }
}

infix fun User.create(builder: PostBuilder): Block {
    return createBlock(builder).apply {
        structure = builder.headerBlock.toJson()
    }
}

infix fun User.create(builder: MomentBuilder): Block {
    return createBlock(builder).apply {
        structure = builder.headerBlock.toJson()
    }
}
//endregion

//region BlockBuilder
abstract class BlockBuilder(
    var type: Int,
    var subtype: Int = 0,
    var tableName: String = "Block"
) {
    var title: String = ""
    var content: String = ""
    var parent: Block? = null
}

class CommentBuilder : BlockBuilder(BLOCK_COMMENT) {
    lateinit var headerBlock: CommentBlock
}

fun Comment(create: CommentBuilder.() -> Unit) = CommentBuilder().apply(create)

class PostBuilder : BlockBuilder(BLOCK_POST) {
    lateinit var headerBlock: PostBlock
}

fun Post(create: PostBuilder.() -> Unit) = PostBuilder().apply(create)

class MomentBuilder : BlockBuilder(BLOCK_MOMENT) {
    lateinit var headerBlock: MomentBlock
}

fun Moment(create: MomentBuilder.() -> Unit) = MomentBuilder().apply(create)

class ForumBuilder : BlockBuilder(BLOCK_FORUM) {
    var headerBlock: ForumBlock = ForumBlock()
}

fun Forum(create: ForumBuilder.() -> Unit) = ForumBuilder().apply(create)

class LeaderBoardBuilder : BlockBuilder(BLOCK_LEADER_BOARD) {
    var headerBlock: LeaderBoardBlock = LeaderBoardBlock()
}

fun LeaderBoard(create: LeaderBoardBuilder.() -> Unit) = LeaderBoardBuilder().apply(create)

class AppBuilder : BlockBuilder(BLOCK_LEADER_BOARD) {
    lateinit var headerBlock: AppBlock
}

fun App(create: AppBuilder.() -> Unit) = AppBuilder().apply(create)

class MailBuilder : BlockBuilder(BLOCK_MAIL) {
    lateinit var headerBlock: MailBlock
}

fun Mail(create: MailBuilder.() -> Unit) = MailBuilder().apply(create)

class ShopBuilder : BlockBuilder(BLOCK_SHOP) {
    lateinit var headerBlock: ShopBlock
}

fun Shop(create: ShopBuilder.() -> Unit) = ShopBuilder().apply(create)

class ItemBuilder : BlockBuilder(BLOCK_ITEM) {
    lateinit var headerBlock: ItemBlock
}

fun Item(create: ItemBuilder.() -> Unit) = ItemBuilder().apply(create)

class ActivityBuilder : BlockBuilder(BLOCK_ACTIVITY) {
    lateinit var headerBlock: ActivityBlock
}

fun Activity(create: ActivityBuilder.() -> Unit) = ActivityBuilder().apply(create)

class TaskBuilder : BlockBuilder(BLOCK_TASK) {
    lateinit var headerBlock: TaskBlock
}

fun Task(create: TaskBuilder.() -> Unit) = TaskBuilder().apply(create)

class TopicBuilder : BlockBuilder(BLOCK_TOPIC) {
    var headerBlock: TopicBlock = TopicBlock()
}

fun Topic(create: TopicBuilder.() -> Unit) = TopicBuilder().apply(create)

class TagBuilder : BlockBuilder(BLOCK_TAG) {
    var headerBlock: TagBlock = TagBlock()
}

fun Tag(create: TagBuilder.() -> Unit) = TagBuilder().apply(create)


class PermissionBuilder(subtype: Int = 0) : BlockBuilder(BLOCK_PERMISSION, subtype)

fun Permission(create: PermissionBuilder.() -> Unit) = PermissionBuilder().apply(create)
fun HunPermission(create: PermissionBuilder.() -> Unit) = PermissionBuilder(PERMISSION_Hun).apply(create)
fun MetaPermission(create: PermissionBuilder.() -> Unit) = PermissionBuilder(PERMISSION_Meta).apply(create)

class RoleBuilder : BlockBuilder(BLOCK_ROLE)

fun Role(create: RoleBuilder.() -> Unit) = RoleBuilder().apply(create)

class IdentityBuilder : BlockBuilder(BLOCK_IDENTITY){
    var headerBlock: IdentityBlock = IdentityBlock()
}

fun Identity(create: IdentityBuilder.() -> Unit) = IdentityBuilder().apply(create)
//endregion