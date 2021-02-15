package com.timecat.data.bmob.data.common

import cn.leancloud.AVObject
import cn.leancloud.annotation.AVClassName
import cn.leancloud.json.JSON
import cn.leancloud.json.JSONObject
import com.timecat.data.bmob.data.User
import com.timecat.identity.data.action.ActionType
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
@AVClassName("Action")
class Action : AVObject("Action"), Serializable {
    //region field
    var user: User
        get() = User.transform(getAVObject("user"))
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
        get() = struct.toString()
        set(value) {
            struct = JSON.parseObject(value)
        }
    var struct: JSONObject
        get() = getJSONObject("structure")
        set(value) {
            put("structure", value)
        }
    var status: Long
        get() = getLong("status")
        set(value) {
            put("status", value)
        }
    //endregion

}

