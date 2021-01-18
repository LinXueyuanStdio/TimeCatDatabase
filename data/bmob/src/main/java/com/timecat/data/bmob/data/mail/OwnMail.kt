package com.timecat.data.bmob.data.mail

import cn.bmob.v3.BmobObject
import com.timecat.data.bmob.data._User
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/15
 * @description 用户拥有物品
 * @usage null
 */
data class OwnMail(
    var user: _User,
    var mail: Mail,
    var receive: Boolean = false,
) : BmobObject("OwnMail"), Serializable {
}