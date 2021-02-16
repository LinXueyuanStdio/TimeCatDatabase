package com.timecat.data.bmob.ext.game

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/13
 * @description null
 * @usage null
 */

const val WHERE_UserExp = "_User.exp"
const val WHERE_UserWater = "_User.water"
const val WHERE_UserCharge = "_User.charge"
const val WHERE_UserMoneyCharge = "_User.moneyCharge"
const val WHERE_UserCurrency = "_User.currency"
const val WHERE_UserStar = "_User.star"
const val WHERE_CubeExp = "OwnCube.exp"
const val WHERE_CubeStar = "OwnCube.star"
const val WHERE_EquipExp = "equip/exp"
const val WHERE_EquipStar = "equip/star"

fun allTaskRules(): List<String> {
    return listOf(
        WHERE_UserExp,
        WHERE_UserWater,
        WHERE_UserCharge,
        WHERE_UserMoneyCharge,
        WHERE_UserCurrency,
        WHERE_UserStar,
        WHERE_CubeExp,
        WHERE_CubeStar,
        WHERE_EquipExp,
        WHERE_EquipStar,
    )
}
