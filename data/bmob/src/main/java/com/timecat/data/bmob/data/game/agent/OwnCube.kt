package com.timecat.data.bmob.data.game.agent

import cn.leancloud.AVObject
import cn.leancloud.annotation.AVClassName
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Block
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/15
 * @description 用户拥有方块
 * @usage null
 */
@AVClassName("OwnCube")
class OwnCube(
    user: User,
    cube: Block,
    exp: Long = 0,//经验
    level: Int = 0,//等级
    star: Int = 0,//星级
    skill: Int = 0,//技能的开启状态
    equipment_1: Block? = null,//装备
    equipment_2: Block? = null,//装备
    equipment_3: Block? = null,//装备
    equipment_4: Block? = null,//装备
    equipment_5: Block? = null,//装备
    equipment_6: Block? = null,//装备
) : AVObject("OwnCube"), Serializable {
    //region field
    var user: User
        get() = User.transform(getAVObject("user"))
        set(value) {
            put("user", value)
        }

    var cube: Block
        get() = getAVObject("cube")
        set(value) {
            put("cube", value)
        }

    var exp: Long
        get() = getLong("exp")
        set(value) {
            put("exp", value)
        }
    var level: Int
        get() = getInt("level")
        set(value) {
            put("level", value)
        }
    var star: Int
        get() = getInt("star")
        set(value) {
            put("star", value)
        }
    var skill: Int
        get() = getInt("skill")
        set(value) {
            put("skill", value)
        }
    var structure: String
        get() = getString("structure")
        set(value) {
            put("structure", value)
        }
    var equipment_1: Block?
        get() = getAVObject("equipment_1")
        set(value) {
            put("equipment_1", value)
        }
    var equipment_2: Block?
        get() = getAVObject("equipment_2")
        set(value) {
            put("equipment_2", value)
        }
    var equipment_3: Block?
        get() = getAVObject("equipment_3")
        set(value) {
            put("equipment_3", value)
        }
    var equipment_4: Block?
        get() = getAVObject("equipment_4")
        set(value) {
            put("equipment_4", value)
        }
    var equipment_5: Block?
        get() = getAVObject("equipment_5")
        set(value) {
            put("equipment_5", value)
        }
    var equipment_6: Block?
        get() = getAVObject("equipment_6")
        set(value) {
            put("equipment_6", value)
        }

    init {
        this.user = user
        this.cube = cube
        this.exp = exp
        this.level = level
        this.star = star
        this.skill = skill
        this.equipment_1 = equipment_1
        this.equipment_2 = equipment_2
        this.equipment_3 = equipment_3
        this.equipment_4 = equipment_4
        this.equipment_5 = equipment_5
        this.equipment_6 = equipment_6
    }
    //endregion
    constructor() : this(User(), Block())

}