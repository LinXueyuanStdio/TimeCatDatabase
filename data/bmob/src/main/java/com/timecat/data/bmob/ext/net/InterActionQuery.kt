package com.timecat.data.bmob.ext.net

import cn.leancloud.AVQuery
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.data.common.InterAction
import com.timecat.identity.data.action.INTERACTION_Auth_Identity
import com.timecat.identity.data.action.INTERACTION_Auth_Permission
import com.timecat.identity.data.action.INTERACTION_Auth_Role
import com.timecat.identity.data.action.INTERACTION_Recommend

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/22
 * @description null
 * @usage null
 */

fun User.allInterActionByType(
    type: List<Int>,
    block: Block? = null
): AVQuery<InterAction> {
    val q = AVQuery<InterAction>("InterAction")
    q.whereEqualTo("user", this)
    if (block != null) q.whereEqualTo("block", block)
    q.whereContainedIn("type", type)
    q.order("-createdAt")
    q.include("user,block,target")
    return q
}

fun User.allInterActionByOneType(type: Int, block: Block? = null) = allInterActionByType(listOf(type), block)
fun User.allRecommendBlockToSomeone(block: Block? = null) = allInterActionByOneType(INTERACTION_Recommend, block)
fun User.allAuthRoleToSomeone(block: Block? = null) = allInterActionByOneType(INTERACTION_Auth_Role, block)
fun User.allAuthIdentityToSomeone(block: Block? = null) = allInterActionByOneType(INTERACTION_Auth_Identity, block)
fun User.allAuthPermissionToSomeone(block: Block? = null) = allInterActionByOneType(INTERACTION_Auth_Permission, block)
fun User.allAuthToSomeone(block: Block? = null) = allInterActionByType(listOf(
    INTERACTION_Auth_Identity, INTERACTION_Auth_Role, INTERACTION_Auth_Permission
), block)

fun User.allInterActionTargetedByType(
    type: Int,
    block: Block? = null
): AVQuery<InterAction> {
    val q = AVQuery<InterAction>("InterAction")
    q.whereEqualTo("target", this)
    if (block != null) q.whereEqualTo("block", block)
    q.whereEqualTo("type", type)
    q.order("-createdAt")
    q.include("user,block,target")
    return q
}

fun User.allInterActionTargetedByType(
    type: List<Int>,
    block: Block? = null
): AVQuery<InterAction> {
    val q = AVQuery<InterAction>("InterAction")
    q.whereEqualTo("target", this)
    if (block != null) q.whereEqualTo("block", block)
    q.whereContainedIn("type", type)
    q.order("-createdAt")
    q.include("user,block,target")
    return q
}

/**
 * 被授予的角色
 */
fun User.allTargetedByAuthRole(block: Block? = null) = allInterActionTargetedByType(INTERACTION_Auth_Role, block)

/**
 * 被授予的身份
 */
fun User.allTargetedByAuthIdentity(block: Block? = null) =
    allInterActionTargetedByType(INTERACTION_Auth_Identity, block)

/**
 * 被授予的权限
 */
fun User.allTargetedByAuthPermission(block: Block? = null) =
    allInterActionTargetedByType(INTERACTION_Auth_Permission, block)

fun User.allTargetedByAuth(block: Block? = null) = allInterActionTargetedByType(
    listOf(
        INTERACTION_Auth_Permission,
        INTERACTION_Auth_Role,
        INTERACTION_Auth_Identity
    ), block
)

fun Block.allInterActionByType(
    type: Int,
    user: User? = null,
    target: User? = null
): AVQuery<InterAction> {
    val q = AVQuery<InterAction>("InterAction")
    q.whereEqualTo("block", this)
    q.order("-createdAt")
    q.whereEqualTo("type", type)
    if (user != null) q.whereEqualTo("user", user)
    if (target != null) q.whereEqualTo("target", target)
    return q
}

fun Block.asRole_allAuthAction(user: User? = null, target: User? = null) =
    allInterActionByType(INTERACTION_Auth_Role, user, target)

fun User.allInterAction(): AVQuery<InterAction> {
    val q = AVQuery<InterAction>("InterAction")
    q.whereEqualTo("user", this)
    q.order("-createdAt")
    return q
}