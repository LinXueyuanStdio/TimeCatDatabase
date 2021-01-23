package com.timecat.data.bmob.data.mail

import cn.leancloud.AVObject
import com.timecat.data.bmob.data._User
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/15
 * @description 用户拥有物品
 * @usage null
 */
class OwnMail(
    user: _User,
    mail: Mail,
    receive: Boolean = false,
) : AVObject("OwnMail"), Serializable {

    var user: _User
        get() = getAVObject("user")
        set(value) {
            put("user", value)
        }
    var mail: Mail
        get() = getAVObject("mail")
        set(value) {
            put("mail", value)
        }
    var receive: Boolean
        get() = getBoolean("receive")
        set(value) {
            put("receive", value)
        }

    init {
        this.user = user
        this.mail = mail
        this.receive = receive
    }
}