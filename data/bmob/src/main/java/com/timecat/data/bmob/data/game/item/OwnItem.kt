package com.timecat.data.bmob.data.game.item

import cn.leancloud.AVObject
import cn.leancloud.annotation.AVClassName
import com.timecat.data.bmob.data._User
import com.timecat.data.bmob.data.common.Block
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/15
 * @description 用户拥有物品
 * @usage null
 */
@AVClassName("OwnItem")
class OwnItem(
    user: _User,
    item: Block,
    count: Int,
) : AVObject("OwnItem"), Serializable {

    var user: _User
        get() = getAVObject("user")
        set(value) {
            put("user", value)
        }
    var item: Block
        get() = getAVObject("item")
        set(value) {
            put("item", value)
        }
    var count: Int
        get() = getInt("count")
        set(value) {
            put("count", value)
        }

    init {
        this.user = user
        this.item = item
        this.count = count
    }

}