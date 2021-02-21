package com.timecat.data.bmob

import android.text.TextUtils
import cn.leancloud.AVObject
import cn.leancloud.AVUser
import cn.leancloud.json.JSONObject
import cn.leancloud.types.AVNull
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.ext.bmob.RequestSingleCallback
import com.timecat.data.bmob.ext.bmob.asUser
import java.util.regex.Pattern
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

/**
 * 验证手机号
 */
fun isMobileNO(mobileNums: String): Boolean {
    /**
     * 判断字符串是否符合手机号码格式
     * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
     * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186
     * 电信号段: 133,149,153,170,173,177,180,181,189
     * @param str
     * @return 待检测的字符串
     */
    val telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$"
    // "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
    return if (TextUtils.isEmpty(mobileNums)) {
        false
    } else {
        mobileNums.matches(telRegex.toRegex())
    }
}

/**
 * 判断email格式是否正确
 */
fun isEmail(email: String): Boolean {
    if (TextUtils.isEmpty(email)) {
        return false
    }
    val str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$"
    val p = Pattern.compile(str)
    return p.matcher(email).matches()
}

fun AVObject.getUserObj(key: String = "user"): User {
    val obj = get(key)
    return when (obj) {
        is User -> obj
        is AVUser -> User.transform(obj)
        is AVObject -> User.transform(obj.toJSONString())
        is JSONObject -> User.transform(obj.toJSONString())
        is Map<*, *> -> obj.asUser()
        else -> obj as User
    }
}