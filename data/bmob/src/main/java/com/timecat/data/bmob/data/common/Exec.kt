package com.timecat.data.bmob.data.common

import cn.leancloud.AVObject
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
class Exec(
    user: User,
    @ExecType
    type: Int = 0,
    structure: String = "",
    status: Long = 0
) : AVObject("Exec"), Serializable {
    companion object {
        fun forRecommend(user: User): Exec {
            return Exec(user, EXEC_Recommend)
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
        this.type = type
        this.structure = structure
        this.status = status
    }
    //endregion

    override fun toString(): String {
        return "${user.objectId} : $type, $status : $structure"
    }
}