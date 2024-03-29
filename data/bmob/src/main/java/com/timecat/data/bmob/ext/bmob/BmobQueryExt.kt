package com.timecat.data.bmob.ext.bmob

import cn.leancloud.AVObject
import cn.leancloud.AVQuery
import cn.leancloud.AVUser
import cn.leancloud.search.AVSearchQuery
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.*
import com.timecat.data.bmob.data.game.*
import com.timecat.data.bmob.data.mail.OwnMail
import com.timecat.data.bmob.ext.toDataError
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description null
 * @usage null
 */
//region 请求实体
fun <T : AVObject> requestList(create: RequestList<T>.() -> Unit) = RequestList<T>().apply(create).build()
fun <T : AVObject> requestOne(create: RequestSingle<T>.() -> Unit) = RequestSingle<T>().apply(create).build()
fun <T : AVObject> requestOneOrNull(create: RequestSingleOrNull<T>.() -> Unit) = RequestSingleOrNull<T>().apply(create).build()

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

class RequestSingleOrNull<T : AVObject> : RequestSingleOrNullCallback<T>() {
    lateinit var query: AVQuery<T>
    fun build(): Disposable {
        query.limit = 1
        return query.findInBackground().flatMap { list: List<T> ->
            if (list.isEmpty()) {
                onEmpty()
            }
            Observable.fromIterable(list)
        }.subscribe({
            onSuccess(it)
        }, {
            onError(it.toDataError())
        }, {
            onComplete()
        })
    }
}

fun requestUser(create: RequestList<AVUser>.() -> Unit) = requestList(create)
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
fun requestPay(create: RequestList<Pay>.() -> Unit) = requestList(create)

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
fun requestOnePay(create: RequestSingle<Pay>.() -> Unit) = requestOne(create)

fun requestOneUserOrNull(create: RequestSingleOrNull<User>.() -> Unit) = requestOneOrNull(create)
fun requestOneBlockOrNull(create: RequestSingleOrNull<Block>.() -> Unit) = requestOneOrNull(create)
fun requestOneActionOrNull(create: RequestSingleOrNull<Action>.() -> Unit) = requestOneOrNull(create)
fun requestOneOwnMailOrNull(create: RequestSingleOrNull<OwnMail>.() -> Unit) = requestOneOrNull(create)
fun requestOneUserRelationOrNull(create: RequestSingleOrNull<User2User>.() -> Unit) = requestOneOrNull(create)
fun requestOneBlockRelationOrNull(create: RequestSingleOrNull<Block2Block>.() -> Unit) = requestOneOrNull(create)
fun requestOneInterActionOrNull(create: RequestSingleOrNull<InterAction>.() -> Unit) = requestOneOrNull(create)
fun requestOneOwnItemOrNull(create: RequestSingleOrNull<OwnItem>.() -> Unit) = requestOneOrNull(create)
fun requestOneOwnCubeOrNull(create: RequestSingleOrNull<OwnCube>.() -> Unit) = requestOneOrNull(create)
fun requestOneOwnActivityOrNull(create: RequestSingleOrNull<OwnActivity>.() -> Unit) = requestOneOrNull(create)
fun requestOneOwnTaskOrNull(create: RequestSingleOrNull<OwnTask>.() -> Unit) = requestOneOrNull(create)
fun requestOnePayOrNull(create: RequestSingleOrNull<Pay>.() -> Unit) = requestOneOrNull(create)
//endregion

//region 是否存在
fun <T : AVObject> requestExist(create: RequestExist<T>.() -> Unit) = RequestExist<T>().apply(create).build()
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
fun requestExistUserRelation(create: RequestExist<User2User>.() -> Unit) = requestExist(create)
fun requestExistBlock(create: RequestExist<Block>.() -> Unit) = requestExist(create)
fun requestExistAction(create: RequestExist<Action>.() -> Unit) = requestExist(create)
fun requestExistUser(create: RequestExist<User>.() -> Unit) = requestExist(create)
//endregion

//region 请求个数
fun <T : AVObject> requestCount(create: RequestCount<T>.() -> Unit) = RequestCount<T>().apply(create).build()

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
fun searchList(create: SearchList.() -> Unit) = SearchList().apply(create).build()

class SearchList : RequestListCallback<AVObject>() {
    lateinit var query: AVSearchQuery<AVObject>
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

fun searchUser(create: SearchList.() -> Unit) = searchList(create)
fun searchBlock(create: SearchList.() -> Unit) = searchList(create)

fun userQuery(q: String?) = AVSearchQuery<AVObject>(q).apply {
    className = "_User"
}

fun blockQuery(q: String?) = AVSearchQuery<AVObject>(q).apply {
    className = "Block"
}
//endregion
