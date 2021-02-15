package com.timecat.data.bmob.data.common

import cn.leancloud.AVObject
import cn.leancloud.json.JSON
import cn.leancloud.json.JSONObject
import com.timecat.data.bmob.data.User
import com.timecat.identity.data.exec.EXEC_Recommend
import com.timecat.identity.data.exec.ExecType
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-15
 * @description 用户行为
 * @usage null
 */
class Exec : AVObject("Exec"), Serializable {
    companion object {
        fun forRecommend(user: User): Exec {
            return Exec().apply {
                this.user = user
                this.type = EXEC_Recommend
                this.type = 0
                this.structure = JSONObject.Builder.create(mapOf()).toString()
                this.status = 0
            }
        }
    }

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