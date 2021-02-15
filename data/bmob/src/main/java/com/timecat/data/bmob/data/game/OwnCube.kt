package com.timecat.data.bmob.data.game

import cn.leancloud.AVObject
import cn.leancloud.annotation.AVClassName
import cn.leancloud.json.JSON
import cn.leancloud.json.JSONObject
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
class OwnCube : AVObject("OwnCube"), Serializable {
    companion object {
        @JvmStatic
        @JvmOverloads
        fun simple(
            user: User,
            cube: Block,
            exp: Long = 0,//经验
            star: Int = 1,//星级
            skill_1: Int = 1,//技能1等级
            skill_2: Int = 1,//技能2等级
            skill_3: Int = 1,//技能3等级
            skill_4: Int = 1,//技能4等级
            skill_5: Int = 1,//技能5等级
            skill_6: Int = 1,//技能6等级
            equipment_1: Block? = null,//装备
            equipment_2: Block? = null,//装备
            equipment_3: Block? = null,//装备
            equipment_4: Block? = null,//装备
            equipment_5: Block? = null,//装备
            equipment_6: Block? = null,//装备
        ): OwnCube {
            return OwnCube().apply {
                this.user = user
                this.cube = cube
                this.exp = exp
                this.star = star
                this.skill_1 = skill_1
                this.skill_2 = skill_2
                this.skill_3 = skill_3
                this.skill_4 = skill_4
                this.skill_5 = skill_5
                this.skill_6 = skill_6
                if (equipment_1 != null) this.equipment_1 = equipment_1
                if (equipment_2 != null) this.equipment_2 = equipment_2
                if (equipment_3 != null) this.equipment_3 = equipment_3
                if (equipment_4 != null) this.equipment_4 = equipment_4
                if (equipment_5 != null) this.equipment_5 = equipment_5
                if (equipment_6 != null) this.equipment_6 = equipment_6
            }
        }
    }

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

    //经验
    var exp: Long
        get() = getLong("exp")
        set(value) {
            put("exp", value)
        }

    //星级
    var star: Int
        get() = getInt("star")
        set(value) {
            put("star", value)
        }

    //技能1等级
    var skill_1: Int
        get() = getInt("skill_1")
        set(value) {
            put("skill_1", value)
        }

    //技能2等级
    var skill_2: Int
        get() = getInt("skill_2")
        set(value) {
            put("skill_2", value)
        }

    //技能3等级
    var skill_3: Int
        get() = getInt("skill_3")
        set(value) {
            put("skill_3", value)
        }

    //技能4等级
    var skill_4: Int
        get() = getInt("skill_4")
        set(value) {
            put("skill_4", value)
        }

    //技能5等级
    var skill_5: Int
        get() = getInt("skill_5")
        set(value) {
            put("skill_5", value)
        }

    //技能6等级
    var skill_6: Int
        get() = getInt("skill_6")
        set(value) {
            put("skill_6", value)
        }
    var structure: String
        get() = struct.toString()
        set(value) {
            struct = JSON.parseObject(value)
        }
    var struct: JSONObject
        get() = getJSONObject("structure")
        set(value) {
            put("structure", value)
        }

    //装备
    var equipment_1: Block?
        get() = getAVObject("equipment_1")
        set(value) {
            put("equipment_1", value)
        }

    //装备
    var equipment_2: Block?
        get() = getAVObject("equipment_2")
        set(value) {
            put("equipment_2", value)
        }

    //装备
    var equipment_3: Block?
        get() = getAVObject("equipment_3")
        set(value) {
            put("equipment_3", value)
        }

    //装备
    var equipment_4: Block?
        get() = getAVObject("equipment_4")
        set(value) {
            put("equipment_4", value)
        }

    //装备
    var equipment_5: Block?
        get() = getAVObject("equipment_5")
        set(value) {
            put("equipment_5", value)
        }

    //装备
    var equipment_6: Block?
        get() = getAVObject("equipment_6")
        set(value) {
            put("equipment_6", value)
        }
    //endregion

}