package com.timecat.data.bmob.ext.net

import cn.leancloud.AVQuery
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Block

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/8
 * @description null
 * @usage null
 */
fun allUsers(query: String? = null) = AVQuery<User>("_User").apply {
    order("-createdAt")
}

fun oneUserOf(id: String) = AVQuery<User>("_User").apply {
    whereEqualTo("objectId", id)
    order("-createdAt")
    setLimit(1)
}

fun blocksOf(user: User) = AVQuery<Block>("Block").apply {
    order("-createdAt")
    whereEqualTo("user", user)
}