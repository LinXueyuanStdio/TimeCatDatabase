package com.timecat.data.bmob.ext.net

import cn.leancloud.AVQuery
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.data.game.Pay

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/8
 * @description null
 * @usage null
 */
fun allPays(query: String? = null) = AVQuery<Pay>("Pay").apply {
    order("-createdAt")
}

fun onePayOf(id: String) = AVQuery<Pay>("Pay").apply {
    whereEqualTo("objectId", id)
    order("-createdAt")
    setLimit(1)
}

fun paysOf(user: User) = AVQuery<Pay>("Pay").apply {
    order("-createdAt")
    whereEqualTo("user", user)
}

fun paysInShopOf(user: User, shop: Block) = AVQuery<Pay>("Pay").apply {
    order("-createdAt")
    whereEqualTo("user", user)
    whereEqualTo("shop", shop)
}
