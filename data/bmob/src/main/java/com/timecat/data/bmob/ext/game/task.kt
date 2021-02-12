package com.timecat.data.bmob.ext.game

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/13
 * @description null
 * @usage null
 */

const val WHERE_UserExp = "user/exp"
const val WHERE_UserWater = "user/water"
const val WHERE_UserCharge = "user/charge"
const val WHERE_UserCurrency = "user/currency"
const val WHERE_UserStar = "user/star"
const val WHERE_CubeExp = "cube/exp"
const val WHERE_CubeStar = "cube/star"
const val WHERE_EquipExp = "equip/exp"
const val WHERE_EquipStar = "equip/star"

fun allTaskRules(): List<String> {
    return listOf(
        WHERE_UserExp,
        WHERE_UserWater,
        WHERE_UserCharge,
        WHERE_UserCurrency,
        WHERE_UserStar,
        WHERE_CubeExp,
        WHERE_CubeStar,
        WHERE_EquipExp,
        WHERE_EquipStar,
    )
}
