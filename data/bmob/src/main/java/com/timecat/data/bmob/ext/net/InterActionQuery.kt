package com.timecat.data.bmob.ext.net

import cn.leancloud.AVQuery
import com.timecat.data.bmob.data._User
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

fun _User.allInterActionByType(
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

fun _User.allInterActionByOneType(type: Int, block: Block? = null) = allInterActionByType(listOf(type), block)
fun _User.allRecommendBlockToSomeone(block: Block? = null) = allInterActionByOneType(INTERACTION_Recommend, block)
fun _User.allAuthRoleToSomeone(block: Block? = null) = allInterActionByOneType(INTERACTION_Auth_Role, block)
fun _User.allAuthIdentityToSomeone(block: Block? = null) = allInterActionByOneType(INTERACTION_Auth_Identity, block)
fun _User.allAuthPermissionToSomeone(block: Block? = null) = allInterActionByOneType(INTERACTION_Auth_Permission, block)
fun _User.allAuthToSomeone(block: Block? = null) = allInterActionByType(listOf(
    INTERACTION_Auth_Identity, INTERACTION_Auth_Role, INTERACTION_Auth_Permission
), block)

fun _User.allInterActionTargetedByType(
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

fun _User.allInterActionTargetedByType(
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
fun _User.allTargetedByAuthRole(block: Block? = null) = allInterActionTargetedByType(INTERACTION_Auth_Role, block)

/**
 * 被授予的身份
 */
fun _User.allTargetedByAuthIdentity(block: Block? = null) =
    allInterActionTargetedByType(INTERACTION_Auth_Identity, block)

/**
 * 被授予的权限
 */
fun _User.allTargetedByAuthPermission(block: Block? = null) =
    allInterActionTargetedByType(INTERACTION_Auth_Permission, block)

fun _User.allTargetedByAuth(block: Block? = null) = allInterActionTargetedByType(
    listOf(
        INTERACTION_Auth_Permission,
        INTERACTION_Auth_Role,
        INTERACTION_Auth_Identity
    ), block
)

fun Block.allInterActionByType(
    type: Int,
    user: _User? = null,
    target: _User? = null
): AVQuery<InterAction> {
    val q = AVQuery<InterAction>("InterAction")
    q.whereEqualTo("block", this)
    q.order("-createdAt")
    q.whereEqualTo("type", type)
    if (user != null) q.whereEqualTo("user", user)
    if (target != null) q.whereEqualTo("target", target)
    return q
}

fun Block.asRole_allAuthAction(user: _User? = null, target: _User? = null) =
    allInterActionByType(INTERACTION_Auth_Role, user, target)

fun _User.allInterAction(): AVQuery<InterAction> {
    val q = AVQuery<InterAction>("InterAction")
    q.whereEqualTo("user", this)
    q.order("-createdAt")
    return q
}