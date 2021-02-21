package com.timecat.data.bmob.ext.bmob

import cn.leancloud.types.AVNull
import com.timecat.data.bmob.data.User
import com.timecat.identity.data.service.DataError

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description null
 * @usage null
 */
open class RequestListCallback<T> {
    var onError: (DataError) -> Unit = {}
    var onEmpty: () -> Unit = {}
    var onSuccess: (List<T>) -> Unit = {}
    var onComplete: () -> Unit = {}
}

open class RequestSingleCallback<T> : SimpleRequestCallback<T>() {
    var onComplete: () -> Unit = {}
}
open class RequestSingleOrNullCallback<T> : RequestSingleCallback<T>() {
    var onEmpty: () -> Unit = {}
}
class EasyRequest : RequestSingleCallback<AVNull>()
class EasyRequestUser : RequestSingleCallback<User>()
class EasyRequestUserNull : RequestSingleCallback<User>()
class EasyRequestUserList : RequestSingleCallback<List<User>>()
open class SimpleRequestCallback<T> {
    var onError: (DataError) -> Unit = {}
    var onSuccess: (T) -> Unit = {}
}