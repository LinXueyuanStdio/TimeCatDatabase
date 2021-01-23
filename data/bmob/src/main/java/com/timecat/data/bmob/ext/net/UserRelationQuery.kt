package com.timecat.data.bmob.ext.net

import cn.leancloud.AVQuery
import com.timecat.data.bmob.data._User
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.data.common.User2User
import com.timecat.identity.data.block.type.BLOCK_COMMENT
import com.timecat.identity.data.block.type.BLOCK_MOMENT
import com.timecat.identity.data.block.type.BLOCK_POST
import com.timecat.identity.data.user_user.User2User_Ding
import com.timecat.identity.data.user_user.User2User_Follow
import com.timecat.identity.data.user_user.User2User_Like
import com.timecat.identity.data.user_user.User2User_Score

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/7
 * @description null
 * @usage null
 */
fun oneUserRelationOf(id: String) = AVQuery<User2User>("User2User").apply {
    whereEqualTo("objectId", id)
    include("user,target")
    order("-createdAt")
    setLimit(1)
}

fun _User.allRelationByType(
    type: Int,
    target: _User? = null
): AVQuery<User2User> {
    val q = AVQuery<User2User>("User2User")
    q.whereEqualTo("author", this)
    q.whereEqualTo("type", type)
    q.order("-createdAt")
    if (target != null) q.whereEqualTo("target", target)
    return q
}

fun allRelationOf(
    target: _User,
    type: Int
): AVQuery<User2User> {
    val q = AVQuery<User2User>("User2User")
    q.whereEqualTo("type", type)
    q.order("-createdAt")
    q.whereEqualTo("target", target)
    return q
}

fun _User.allLike(target: _User? = null) = allRelationByType(User2User_Like, target)
fun _User.allDing(target: _User? = null) = allRelationByType(User2User_Ding, target)
fun _User.allScore(target: _User? = null) = allRelationByType(User2User_Score, target)
fun _User.allFollow(target: _User? = null) = allRelationByType(User2User_Follow, target)
fun fansOf(user: _User) = allRelationOf(user, User2User_Follow)


fun childrenOf(user: _User, type: List<Int>) = AVQuery<Block>("Block").apply {
    whereEqualTo("user", user)
    include("user,parent")
    order("-createdAt")
    whereContainedIn("type", type)
}

fun _User.findAllPost() = childrenOf(this, listOf(BLOCK_POST))
fun _User.findAllMoment() = childrenOf(this, listOf(BLOCK_MOMENT))
fun _User.findAllComment() = childrenOf(this, listOf(BLOCK_COMMENT))
