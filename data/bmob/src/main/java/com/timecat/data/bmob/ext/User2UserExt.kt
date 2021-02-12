package com.timecat.data.bmob.ext

import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.User2User
import com.timecat.identity.data.user_user.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/7
 * @description null
 * @usage null
 */
fun relation(source: User, target: User, @User2UserType type: Int) = User2User().apply {
    this.author = source
    this.target = target
    this.type = type
    this.structure = ""
    this.status = 0
}

infix fun User.like(target: User): User2User = relation(this, target, User2User_Like)
infix fun User.follow(target: User): User2User = relation(this, target, User2User_Follow)
infix fun User.ding(target: User): User2User = relation(this, target, User2User_Ding)
infix fun User.score(target: User): User2User = relation(this, target, User2User_Score)

