package com.timecat.data.bmob.data.common

import android.os.Parcel
import android.os.Parcelable
import cn.leancloud.AVObject
import cn.leancloud.Transformer
import cn.leancloud.annotation.AVClassName
import com.timecat.data.bmob.data.User
import com.timecat.identity.data.action.ActionType
import com.timecat.identity.data.base.IStatus
import com.timecat.identity.data.block.type.*
import org.joda.time.DateTime

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-05
 * @description 价值：笔记、任务、习惯、计划
 * @usage
 *   type 必须线性增长，当 View 渲染大于某个数的 type 表示当前版本过低，收到较高版本，需要用户升级
 *   status 因 type 的不同而不同 写在 TaskHeader 里查询不方便，因为很多都有 delete 状态
 */
@AVClassName("Block")
class Block : AVObject("Block"), Parcelable, IStatus {

    //region field
    var user: User
        get() = User.transform(getAVObject("user"))
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
        get() = this.getString("title")
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
    var parent: Block?
        get() = getAVObject("parent")
        set(value) {
            put("parent", value)
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

    //region Parcelable
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(toJSONString())
    }

    companion object CREATOR : Parcelable.Creator<Block> {
        override fun createFromParcel(parcel: Parcel): Block {
            val jsonString = parcel.readString()
            val rawObject = parseAVObject(jsonString)
            return Transformer.transform(rawObject, Block::class.java)
        }

        override fun newArray(size: Int): Array<Block?> {
            return arrayOfNulls(size)
        }

        @JvmOverloads
        fun forName(user: User, type: Int, name: String = ""): Block {
            return Block().apply {
                this.user = user
                this.type = type
                this.title = name
                this.content = name
                this.subtype = 0
                this.structure = "{}"
                this.status = 0
                this.likes = 0
                this.comments = 0
                this.stars = 0
                this.relays = 0
                this.followers = 0
                this.usedBy = 0
                this.parent = null
            }
        }

        fun forMoment(user: User, content: String): Block {
            return forName(user, BLOCK_MOMENT, content)
        }

        fun forPost(user: User, content: String): Block {
            return forName(user, BLOCK_POST, content)
        }

        fun forComment(user: User, content: String): Block {
            return forName(user, BLOCK_COMMENT, content)
        }

        fun forApp(user: User, content: String): Block {
            return forName(user, BLOCK_APP, content)
        }
    }
    //endregion

}

