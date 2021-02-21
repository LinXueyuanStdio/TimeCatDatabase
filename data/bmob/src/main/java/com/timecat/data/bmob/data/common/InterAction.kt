package com.timecat.data.bmob.data.common

import cn.leancloud.AVObject
import cn.leancloud.annotation.AVClassName
import cn.leancloud.json.JSON
import cn.leancloud.json.JSONObject
import cn.leancloud.types.AVDate
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.getUserObj
import com.timecat.identity.data.action.InterActionType
import org.joda.time.DateTime
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-05
 * @description 两个用户的交互动作，该动作涉及一个块
 * 用户A，C
 * 块 B
 * 举例：
 * A 将 B 推荐给 C
 * A 将 B 赠送给 C
 * A 将 B 分配给 C
 * A 将 B 授予给 C（授权）
 * @usage
 *   status 因 type 的不同而不同，很多都有 delete 状态
 */
@AVClassName("InterAction")
class InterAction : AVObject("InterAction"), Serializable {

    //region field
    var user: User
        get() = getUserObj()
        set(value) {
            put("user", value)
        }
    var block: Block
        get() = getAVObject("block")
        set(value) {
            put("block", value)
        }
    var target: User
        get() = getAVObject("target")
        set(value) {
            put("target", value)
        }
    var type: Int
        get() = getInt("type")
        set(value) {
            put("type", value)
        }
    var activeTime: AVDate
        get() = get("activeTime") as AVDate
        set(value) {
            put("activeTime", value)
        }
    var expireTime: AVDate
        get() = get("expireTime") as AVDate
        set(value) {
            put("expireTime", value)
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

    var activeDateTime: DateTime
        get() = DateTime(activeTime.date)
        set(value) {
            activeTime = AVDate(value.toString(AVDate.DEFAULT_FORMAT))
        }
    var expireDateTime: DateTime
        get() = DateTime(expireTime.date)
        set(value) {
            expireTime = AVDate(value.toString(AVDate.DEFAULT_FORMAT))
        }
}

