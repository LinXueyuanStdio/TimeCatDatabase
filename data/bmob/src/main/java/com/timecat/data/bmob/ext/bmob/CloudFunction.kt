package com.timecat.data.bmob.ext.bmob

import cn.leancloud.AVCloud
import cn.leancloud.AVObject
import cn.leancloud.Transformer
import cn.leancloud.json.JSONObject
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.toDataError
import io.reactivex.disposables.Disposable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/15
 * @description null
 * @usage null
 */

class CloudFunction<T>(
    val functionName: String
) : RequestSingleCallback<T>() {
    lateinit var params: MutableMap<String, Any>
    fun build(): Disposable {
        return AVCloud.callFunctionInBackground<T>(functionName, params)
            .subscribe({
                onSuccess(it)
            }, {
                onError(it.toDataError())
            }, {
                onComplete()
            })
    }
}

fun <T> cloudFunction(functionName: String, create: CloudFunction<T>.() -> Unit) = CloudFunction<T>(functionName).apply(create).build()

fun <T> useItem(ownItemId: String, count: Int, create: CloudFunction<T>.() -> Unit): Disposable =
    cloudFunction<T>("useItem") {
        params = mutableMapOf()
        params["ownItemId"] = ownItemId
        params["count"] = count
        apply(create)
    }

fun <T> useItem(
    ownItemId: String,
    count: Int,
    targetId: String,
    create: CloudFunction<T>.() -> Unit
): Disposable = cloudFunction<T>("useItem") {
    params = mutableMapOf()
    params["ownItemId"] = ownItemId
    params["count"] = count
    params["targetId"] = targetId
    apply(create)
}

fun <T> buyItem(
    goodId: String,
    count: Int,
    value: Int,
    moneyId: String,
    create: CloudFunction<T>.() -> Unit
): Disposable = cloudFunction<T>("buyItem") {
    params = mutableMapOf()
    params["goodId"] = goodId
    params["count"] = count
    params["value"] = value
    params["moneyId"] = moneyId
    apply(create)
}

fun Any.asBlock(): Block {
    val json = JSONObject.Builder.create(this as Map<String, Any>)
    val rawObject = AVObject.parseAVObject(json.toJSONString())
    return Transformer.transform(rawObject, Block::class.java)
}

fun Any.asUser(): User {
    val json = JSONObject.Builder.create(this as Map<String, Any>)
    val rawObject = AVObject.parseAVObject(json.toJSONString())
    return Transformer.transform(rawObject, User::class.java)
}

fun findAllComments(
    skip: Int,
    pageSize: Int,
    blockId: String,
    create: CloudFunction<List<HashMap<String, Any>>>.() -> Unit): Disposable {
    return cloudFunction<List<HashMap<String, Any>>>("findAllComments") {
        params = mutableMapOf()
        params["skip"] = skip
        params["pageSize"] = pageSize
        params["blockId"] = blockId
        apply(create)
    }
}
