package com.timecat.data.bmob.ext

import com.timecat.identity.data.service.DataError

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-11-25
 * @description null
 * @usage null
 */
fun Throwable.toDataError(): DataError = DataError(0, this.message)
