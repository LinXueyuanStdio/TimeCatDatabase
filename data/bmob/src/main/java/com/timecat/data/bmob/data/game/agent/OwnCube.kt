package com.timecat.data.bmob.data.game.agent

import cn.bmob.v3.BmobObject
import com.timecat.data.bmob.data._User
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.data.game.item.Item
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/15
 * @description 用户拥有方块
 * @usage null
 */
data class OwnCube(
    var user: _User,
    var cube: Block,
    var level: Int = 0,//等级
    val exp: Long = 0,//经验
    val star: Int = 0,//星级
    val skill: Int = 0,//技能的开启状态
    val equipment_1: Block? = null,//装备
    val equipment_2: Block? = null,//装备
    val equipment_3: Block? = null,//装备
    val equipment_4: Block? = null,//装备
    val equipment_5: Block? = null,//装备
    val equipment_6: Block? = null,//装备
) : BmobObject("OwnCube"), Serializable {
}