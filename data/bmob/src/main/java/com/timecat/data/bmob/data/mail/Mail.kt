package com.timecat.data.bmob.data.mail

import cn.leancloud.AVObject
import com.timecat.data.bmob.data.common.Block
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-08
 * @description 邮件
 * @usage null
 */
class Mail(
    /**
     * 邮件名称
     */
    name: String = "",
    /**
     * 邮件内容
     */
    content: String = "",
    /**
     * 邮件类型
     */
    type: Int = 0,
    /**
     * 邮件奖励描述：{ items }
     */
    items: MutableList<Block> = mutableListOf()
) : AVObject("Mail"), Serializable {
    var name: String
        get() = getString("name")
        set(value) {
            put("name", value)
        }
    var content: String
        get() = getString("content")
        set(value) {
            put("content", value)
        }
    var type: Int
        get() = getInt("type")
        set(value) {
            put("type", value)
        }
    var items: MutableList<Block>
        get() = getList("items") as MutableList<Block>
        set(value) {
            put("items", value)
        }

    init {
        this.name = name
        this.content = content
        this.type = type
        this.items = items
    }
}
