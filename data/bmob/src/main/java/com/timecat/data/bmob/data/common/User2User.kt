package com.timecat.data.bmob.data.common

import cn.leancloud.AVObject
import cn.leancloud.annotation.AVClassName
import com.timecat.data.bmob.data._User
import com.timecat.identity.data.user_user.User2UserType
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/7
 * @description 人与人之间的关系
 * @usage null
 */
@AVClassName("User2User")
class User2User(
    author: _User,
    target: _User,
    @User2UserType
    type: Int = 0,
    structure: String = "",
    status: Long = 0
) : AVObject("User2User"), Serializable {
    //region field
    var author: _User
        get() = getAVObject("author")
        set(value) {
            put("author", value)
        }

    var target: _User
        get() = getAVObject("target")
        set(value) {
            put("target", value)
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
        this.author = author
        this.target = target
        this.type = type
        this.structure = structure
        this.status = status
    }
    //endregion

}
