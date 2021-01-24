package com.timecat.data.bmob.data.mail

import cn.leancloud.AVObject
import cn.leancloud.annotation.AVClassName
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Block
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/15
 * @description 用户拥有物品
 * @usage null
 */
@AVClassName("OwnMail")
class OwnMail(
    user: User,
    mail: Block,
    receive: Boolean = false,
) : AVObject("OwnMail"), Serializable {

    var user: User
        get() = User.transform(getAVObject("user"))
        set(value) {
            put("user", value)
        }
    var mail: Block
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
    constructor() : this(User(), Block())

}