package com.timecat.data.bmob.ext.bmob

import cn.leancloud.types.AVNull
import com.timecat.data.bmob.data._User
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
}

open class RequestSingleCallback<T> : SimpleRequestCallback<T>()
class EasyRequest : RequestSingleCallback<AVNull>()
class EasyRequestUser : RequestSingleCallback<_User>()
class EasyRequestUserNull : RequestSingleCallback<_User?>()
class EasyRequestUserList : RequestSingleCallback<List<_User>>()
open class SimpleRequestCallback<T> {
    var onError: (DataError) -> Unit = {}
    var onSuccess: (T) -> Unit = {}
}