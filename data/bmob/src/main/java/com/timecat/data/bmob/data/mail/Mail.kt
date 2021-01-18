package com.timecat.data.bmob.data.mail

import cn.bmob.v3.BmobObject
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.data.game.item.Item
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-08
 * @description 邮件
 * @usage null
 */
data class Mail(
    /**
     * 邮件名称
     */
    var name: String = "",
    /**
     * 邮件内容
     */
    var content: String = "",
    /**
     * 邮件类型
     */
    var type: Int = 0,
    /**
     * 邮件奖励描述：{ items }
     */
    var items: List<Block> = emptyList()
) : BmobObject("Mail"), Serializable {

}
