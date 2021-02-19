package com.timecat.data.bmob.ext.bmob

import cn.leancloud.AVCloud
import com.timecat.data.bmob.ext.toDataError
import io.reactivex.disposables.Disposable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/15
 * @description null
 * @usage null
 */

class CloudFunction<T> : RequestSingleCallback<T>() {
    lateinit var params: MutableMap<String, Any>
    fun build(): Disposable {
        return AVCloud.callFunctionInBackground<T>("useItem", params)
            .subscribe({
                onSuccess(it)
            }, {
                onError(it.toDataError())
            }, {
                onComplete()
            })
    }
}

fun <T> cloudFunction(create: CloudFunction<T>.() -> Unit) = CloudFunction<T>().apply(create).build()

fun <T> useItem(ownItemId: String, count: Int, create: CloudFunction<T>.() -> Unit): Disposable = cloudFunction<T> {
    params = mutableMapOf()
    params["ownItemId"] = ownItemId
    params["count"] = count
    apply(create)
}

fun <T> useItem(
    ownItemId: String,
    count: Int,
    targetId:String,
    create: CloudFunction<T>.() -> Unit
): Disposable = cloudFunction<T> {
    params = mutableMapOf()
    params["ownItemId"] = ownItemId
    params["count"] = count
    params["targetId"] = targetId
    apply(create)
}
