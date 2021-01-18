package com.timecat.data.bmob.data.game.item

import cn.bmob.v3.BmobObject
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-08
 * @description 礼包 道具 装备 角色碎片 装备碎片
 * @usage null
 */
data class Item(
    /**
     * 物品名称
     */
    var name: String,
    /**
     * 物品图标
     */
    var icon: String,
    /**
     * 物品描述
     */
    var description: String,
    /**
     * 物品类型
     */
    var type: Int,
    /**
     * 物品结构描述
     * 礼包：{ items }
     * 随机宝箱：{ items:[Item], prob_of_each_item:[int] }
     */
    var structure: String
) : BmobObject("Item"), Serializable {

}
