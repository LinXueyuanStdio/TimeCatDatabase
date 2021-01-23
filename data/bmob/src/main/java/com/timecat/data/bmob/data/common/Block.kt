package com.timecat.data.bmob.data.common

import cn.leancloud.AVObject
import com.alibaba.fastjson.JSONObject
import com.timecat.data.bmob.data._User
import com.timecat.identity.data.base.IStatus
import com.timecat.identity.data.base.Json
import com.timecat.identity.data.base.PrivacyScope
import com.timecat.identity.data.block.type.*
import org.joda.time.DateTime
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-05
 * @description 价值：笔记、任务、习惯、计划
 * @usage
 *   type 必须线性增长，当 View 渲染大于某个数的 type 表示当前版本过低，收到较高版本，需要用户升级
 *   status 因 type 的不同而不同 写在 TaskHeader 里查询不方便，因为很多都有 delete 状态
 */
class Block(
    user: _User,
    @BlockType
    type: Int,
    subtype: Int = 0,

    title: String = "",
    content: String = "",

    structure: String = "{}",
    status: Long = 0,//不同类型 type 对 status 的解释不同
    /**
     * 点赞数
     */
    likes: Int = 0,
    /**
     * 评论数
     */
    comments: Int = 0,
    /**
     * 收藏数
     */
    stars: Int = 0,
    /**
     * 转发数
     */
    relays: Int = 0,
    /**
     * 关注数
     */
    followers: Int = 0,
    /**
     * 浏览量
     */
    usedBy: Int = 0, // 纯计数，单调递增 浏览量
    /**
     * 排序
     */
    order: Int = 0,
    /**
     * 隐私域
     */
    privacy: PrivacyScope = PrivacyScope(isPrivate = true),
    /**
     * 扩展
     */
    ext: Json = Json(JSONObject()),
    /**
     * Block 父节点
     */
    parent: Block? = null
) : AVObject("Block"), Serializable, IStatus {

    companion object {
        @JvmOverloads
        fun forName(user: _User, type: Int, name: String = ""): Block {
            return Block(user, type, title = name, content = name)
        }

        fun forMoment(user: _User, content: String): Block {
            return forName(user, BLOCK_MOMENT, content)
        }

        fun forPost(user: _User, content: String): Block {
            return forName(user, BLOCK_POST, content)
        }

        fun forComment(user: _User, content: String): Block {
            return forName(user, BLOCK_COMMENT, content)
        }

        fun forApp(user: _User, content: String): Block {
            return forName(user, BLOCK_APP, content)
        }
    }

    //region field
    var user: _User
        get() = getAVObject("user")
        set(value) {
            put("user", value)
        }

    var type: Int
        get() = getInt("type")
        set(value) {
            put("type", value)
        }
    var subtype: Int
        get() = getInt("subtype")
        set(value) {
            put("subtype", value)
        }
    var title: String
        get() = getString("title")
        set(value) {
            put("title", value)
        }
    var content: String
        get() = getString("content")
        set(value) {
            put("content", value)
        }

    var structure: String
        get() = getString("structure")
        set(value) {
            put("structure", value)
        }

    var status: Long
        get() = getLong("status")
        set(value) {
            put("status", value)
        }
    var likes: Int
        get() = getInt("likes")
        set(value) {
            put("likes", value)
        }
    var comments: Int
        get() = getInt("comments")
        set(value) {
            put("comments", value)
        }
    var stars: Int
        get() = getInt("stars")
        set(value) {
            put("stars", value)
        }
    var relays: Int
        get() = getInt("relays")
        set(value) {
            put("relays", value)
        }
    var followers: Int
        get() = getInt("followers")
        set(value) {
            put("followers", value)
        }
    var usedBy: Int
        get() = getInt("usedBy")
        set(value) {
            put("usedBy", value)
        }
    var order: Int
        get() = getInt("order")
        set(value) {
            put("order", value)
        }
    var privacy: PrivacyScope
        get() = get("privacy") as PrivacyScope
        set(value) {
            put("privacy", value)
        }
    var ext: Json
        get() = get("ext") as Json
        set(value) {
            put("ext", value)
        }
    var parent: Block?
        get() = getAVObject("parent")
        set(value) {
            put("parent", value)
        }

    init {
        this.user = user
        this.type = type
        this.subtype = subtype
        this.title = title
        this.content = content
        this.structure = structure
        this.status = status
        this.likes = likes
        this.comments = comments
        this.stars = stars
        this.relays = relays
        this.followers = followers
        this.usedBy = usedBy
        this.order = order
        this.privacy = privacy
        this.ext = ext
        this.parent = parent
    }
    //endregion

    fun isComment(): Boolean = type == BLOCK_COMMENT

    fun isApp(): Boolean = type == BLOCK_APP

    fun getCreatedDateTime(): DateTime = DateTime(createdAt)
    fun getUpdatedDateTime(): DateTime = DateTime(updatedAt)

    fun eq(other: Block): Boolean {
        return other.objectId == objectId
    }

    //region IStatus 用 16 进制管理状态
    /**
     * 往状态集中加一个状态
     * @param status status
     */
    override fun addStatus(status: Long) {
        this.status = this.status or status
    }

    /**
     * 往状态集中移除一个状态
     * @param status status
     */
    override fun removeStatus(status: Long) {
        this.status = this.status and status.inv()
    }

    /**
     * 状态集中是否包含某状态
     * @param status status
     */
    override fun isStatusEnabled(status: Long): Boolean {
        return this.status and status != 0L
    }

    override fun statusDescription(): String = ""
    //endregion

    override fun toString(): String {
        return "$objectId(type=$type, subtype=$subtype, title=$title, content=$content,\n" +
            "         structure='$structure', status=${statusDescription()}, likes=$likes, comments=$comments, relays=$relays, usedBy=$usedBy, order=$order,\n" +
            "         parent=${parent?.objectId}, user=${user?.objectId})\n"
    }
}

