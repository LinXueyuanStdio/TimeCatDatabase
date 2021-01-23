package com.timecat.data.bmob

import cn.leancloud.types.AVNull
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.ext.bmob.RequestSingleCallback
import kotlin.random.Random


/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/10
 * @description null
 * @usage null
 */
fun randomPageSize() = Random.nextInt(9, 11)
const val pageSize = 10
