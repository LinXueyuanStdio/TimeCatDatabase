package com.timecat.data.bmob.ext.bmob

import cn.leancloud.AVObject
import cn.leancloud.AVQuery
import com.timecat.data.bmob.data._User
import com.timecat.data.bmob.data.common.*
import com.timecat.data.bmob.data.game.agent.OwnCube
import com.timecat.data.bmob.data.game.item.OwnItem
import com.timecat.data.bmob.data.mail.OwnMail
import com.timecat.data.bmob.ext.toDataError
import io.reactivex.disposables.Disposable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description null
 * @usage null
 */
//region 请求实体
fun <T : AVObject> requestList(create: RequestList<T>.() -> Unit) = RequestList<T>().apply(create).also { it.build() }
fun <T : AVObject> requestOne(create: RequestSingle<T>.() -> Unit) = RequestSingle<T>().apply(create).also { it.build() }

class RequestList<T : AVObject> : RequestListCallback<T>() {
    lateinit var query: AVQuery<T>
    fun build(): Disposable {
        return query.findInBackground()
            .subscribe({
            if (it.isEmpty()) {
                onEmpty()
            } else {
                onSuccess(it)
            }
        }, {
            onError(it.toDataError())
        })
    }
}

class RequestSingle<T : AVObject> : RequestSingleCallback<T>() {
    lateinit var query: AVQuery<T>
    fun build(): Disposable {
        return query.firstInBackground.subscribe({
            onSuccess(it)
        }, {
            onError(it.toDataError())
        })
    }
}

fun requestUser(create: RequestList<_User>.() -> Unit) = requestList(create)
fun requestBlock(create: RequestList<Block>.() -> Unit) = requestList(create)
fun requestAction(create: RequestList<Action>.() -> Unit) = requestList(create)
fun requestOwnMail(create: RequestList<OwnMail>.() -> Unit) = requestList(create)
fun requestUserRelation(create: RequestList<User2User>.() -> Unit) = requestList(create)
fun requestBlockRelation(create: RequestList<Block2Block>.() -> Unit) = requestList(create)
fun requestInterAction(create: RequestList<InterAction>.() -> Unit) = requestList(create)
fun requestOwnItem(create: RequestList<OwnItem>.() -> Unit) = requestList(create)
fun requestOwnCube(create: RequestList<OwnCube>.() -> Unit) = requestList(create)
//endregion

//region 是否存在
fun <T : AVObject> requestExist(create: RequestExist<T>.() -> Unit) = RequestExist<T>().apply(create).also { it.build() }
class RequestExist<T : AVObject> : SimpleRequestCallback<Boolean>() {
    lateinit var query: AVQuery<T>
    fun build(): Disposable {
        return query.countInBackground().subscribe({
            if (it > 0) {
                onSuccess(true)
            } else {
                onSuccess(false)
            }
        }, {
            onError(it.toDataError())
        })
    }
}

fun requestExistInterAction(create: RequestExist<InterAction>.() -> Unit) = requestExist(create)
fun requestExistBlock(create: RequestExist<Block>.() -> Unit) = requestExist(create)
fun requestExistAction(create: RequestExist<Action>.() -> Unit) = requestExist(create)
fun requestExistUser(create: RequestExist<_User>.() -> Unit) = requestExist(create)
//endregion

//region 请求个数
fun <T : AVObject> requestCount(create: RequestCount<T>.() -> Unit) = RequestCount<T>().apply(create).also { it.build() }

class RequestCount<T : AVObject> : SimpleRequestCallback<Int>() {
    lateinit var query: AVQuery<T>
    fun build(): Disposable {
        return query.countInBackground().subscribe({
            onSuccess(it)
        }, {
            onError(it.toDataError())
        })
    }
}

fun requestActionCount(create: RequestCount<Action>.() -> Unit) = requestCount(create)
fun requestBlockCount(create: RequestCount<Block>.() -> Unit) = requestCount(create)
fun requestUserRelationCount(create: RequestCount<User2User>.() -> Unit) = requestCount(create)
//endregion
