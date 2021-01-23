package com.timecat.data.bmob.data.common

import cn.leancloud.AVObject
import com.timecat.data.bmob.data._User
import com.timecat.identity.data.action.ActionType
import com.timecat.identity.data.action.InterActionType
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-05
 * @description 动作：投票、点赞、打卡、时间块记录
 * Action 的目标是 Block，不是 Action。
 * @usage
 *   status 因 type 的不同而不同，很多都有 delete 状态
 */
class Action(
    user: _User,
    block: Block,
    @ActionType
    type: Int = 0,
    structure: String = "",
    status: Long = 0
    //var like: Int, // 动作的点赞数0，如坚持打卡的点赞数
) : AVObject("Action"), Serializable {
    //region field
    var user: _User
        get() = getAVObject("user")
        set(value) {
            put("user", value)
        }

    var block: Block
        get() = getAVObject("block")
        set(value) {
            put("block", value)
        }

    var type: Int
        get() = getInt("type")
        set(value) {
            put("type", value)
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

    init {
        this.user = user
        this.block = block
        this.type = type
        this.structure = structure
        this.status = status
    }
    //endregion

}

