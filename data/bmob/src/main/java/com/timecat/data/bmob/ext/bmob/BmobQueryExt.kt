package com.timecat.data.bmob.ext.bmob

import cn.leancloud.AVObject
import cn.leancloud.AVQuery
import cn.leancloud.search.AVSearchQuery
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.*
import com.timecat.data.bmob.data.game.OwnActivity
import com.timecat.data.bmob.data.game.OwnCube
import com.timecat.data.bmob.data.game.OwnItem
import com.timecat.data.bmob.data.game.OwnTask
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
            }, {
                onComplete()
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
        }, {
            onComplete()
        })
    }
}

fun requestUser(create: RequestList<User>.() -> Unit) = requestList(create)
fun requestBlock(create: RequestList<Block>.() -> Unit) = requestList(create)
fun requestAction(create: RequestList<Action>.() -> Unit) = requestList(create)
fun requestOwnMail(create: RequestList<OwnMail>.() -> Unit) = requestList(create)
fun requestUserRelation(create: RequestList<User2User>.() -> Unit) = requestList(create)
fun requestBlockRelation(create: RequestList<Block2Block>.() -> Unit) = requestList(create)
fun requestInterAction(create: RequestList<InterAction>.() -> Unit) = requestList(create)
fun requestOwnItem(create: RequestList<OwnItem>.() -> Unit) = requestList(create)
fun requestOwnCube(create: RequestList<OwnCube>.() -> Unit) = requestList(create)
fun requestOwnActivity(create: RequestList<OwnActivity>.() -> Unit) = requestList(create)
fun requestOwnTask(create: RequestList<OwnTask>.() -> Unit) = requestList(create)

fun requestOneUser(create: RequestSingle<User>.() -> Unit) = requestOne(create)
fun requestOneBlock(create: RequestSingle<Block>.() -> Unit) = requestOne(create)
fun requestOneAction(create: RequestSingle<Action>.() -> Unit) = requestOne(create)
fun requestOneOwnMail(create: RequestSingle<OwnMail>.() -> Unit) = requestOne(create)
fun requestOneUserRelation(create: RequestSingle<User2User>.() -> Unit) = requestOne(create)
fun requestOneBlockRelation(create: RequestSingle<Block2Block>.() -> Unit) = requestOne(create)
fun requestOneInterAction(create: RequestSingle<InterAction>.() -> Unit) = requestOne(create)
fun requestOneOwnItem(create: RequestSingle<OwnItem>.() -> Unit) = requestOne(create)
fun requestOneOwnCube(create: RequestSingle<OwnCube>.() -> Unit) = requestOne(create)
fun requestOneOwnActivity(create: RequestSingle<OwnActivity>.() -> Unit) = requestOne(create)
fun requestOneOwnTask(create: RequestSingle<OwnTask>.() -> Unit) = requestOne(create)
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
fun requestExistUser(create: RequestExist<User>.() -> Unit) = requestExist(create)
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

//region 搜索
fun <T : AVObject> searchList(create: SearchList<T>.() -> Unit) = SearchList<T>().apply(create).also { it.build() }

class SearchList<T : AVObject> : RequestListCallback<T>() {
    lateinit var query: AVSearchQuery<T>
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

fun searchUser(create: SearchList<User>.() -> Unit) = searchList(create)
fun searchBlock(create: SearchList<Block>.() -> Unit) = searchList(create)

fun userQuery(q: String?) = AVSearchQuery<User>(q)
fun blockQuery(q: String?) = AVSearchQuery<Block>(q)
//endregion