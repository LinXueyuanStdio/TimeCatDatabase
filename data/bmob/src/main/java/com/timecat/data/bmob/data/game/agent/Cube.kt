package com.timecat.data.bmob.data.game.agent

import cn.leancloud.AVObject
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-08
 * @description 方块
 * @usage null
 */
data class Cube(
    /**
     * 方块名称
     */
    var name: String,
    /**
     * 方块图标
     */
    var icon: String,
    /**
     * 方块描述
     */
    var description: String,
    /**
     * 方块类型
     */
    var type: Int,
    /**
     * 方块结构描述
     */
    var structure: String
) : AVObject("Cube"), Serializable {

}
