package com.timecat.data.bmob.data

import android.text.TextUtils
import cn.leancloud.AVFile
import cn.leancloud.AVUser
import cn.leancloud.annotation.AVClassName
import java.io.Serializable
import java.util.regex.Pattern

/**
 * Created by yc on 2018/2/2.
 */
@AVClassName("_User")
class _User() : AVUser(), Serializable {
    //region getter and setter
    //个性
    //头像
    var headPortrait: AVFile?
        get() = getAVFile("headPortrait")
        set(value) {
            put("headPortrait", value)
        }

    //背景图
    var coverPage: AVFile?
        get() = getAVFile("coverPage")
        set(value) {
            put("coverPage", value)
        }

    //个性签名
    var brief_intro: String
        get() = getString("brief_intro") ?: ""
        set(value) {
            put("brief_intro", value)
        }
    var nickName: String
        get() = getString("nickName") ?: "薛定谔的喵"
        set(value) {
            put("nickName", value)
        }

    //个人信息
    var gender: String
        get() = getString("gender") ?: ""
        set(value) {
            put("gender", value)
        }
    var province: String
        get() = getString("province") ?: ""
        set(value) {
            put("province", value)
        }

    //IM
    var token: String?
        get() = getString("token")
        set(value) {
            put("token", value)
        }
    var code: String?
        get() = getString("code")
        set(value) {
            put("code", value)
        }

    //游戏化
    //等级
    var level: Int
        get() = getInt("level")
        set(value) {
            put("level", value)
        }

    //经验
    var exp: Long
        get() = getLong("exp")
        set(value) {
            put("exp", value)
        }

    //星级
    var star: Int
        get() = getInt("star")
        set(value) {
            put("star", value)
        }

    //体力
    var water: Int
        get() = getInt("water")
        set(value) {
            put("water", value)
        }

    //上次体力的结算时间
    var last_settle_time: Int
        get() = getInt("last_settle_time")
        set(value) {
            put("last_settle_time", value)
        }

    //通用货币,用于抽取角色
    var currency: Long
        get() = getLong("currency")
        set(value) {
            put("currency", value)
        }

    //充值货币
    var charge: Long
        get() = getLong("charge")
        set(value) {
            put("charge", value)
        }

    constructor(objId: String?) : this() {
        setObjectId(objId)
    }

    fun setheadPortrait(headPortrait: AVFile?) {
        this.headPortrait = headPortrait
    }

    val nick: String
        get() = nickName
    val userid: String
        get() = getObjectId()
    var avatar: String
        get() = if (headPortrait == null) {
            "http://bmob-cdn-22390.b0.upaiyun.com/2019/04/17/531db7e5405a08ae8061b800ae8ad3b6.jpg"
        } else headPortrait!!.url
        set(avatar) {
            headPortrait = AVFile("avatar", avatar)
        }
    var cover: String
        get() = if (coverPage == null) {
            "http://d.hiphotos.baidu.com/zhidao/pic/item/bf096b63f6246b601ffeb44be9f81a4c510fa218.jpg"
        } else coverPage!!.url
        set(cover) {
            headPortrait = AVFile("cover", cover)
        }
    var nickname: String
        get() = nick
        set(nickname) {
            nickName = nickname
        }

    var signature: String
        get() = brief_intro
        set(signature) {
            brief_intro = signature
        }
    val id: String
        get() = getObjectId()

    //endregion
    override fun toString(): String {
        return "_User{" +
            "objId=" + getObjectId() +
            ", headPortrait=" + headPortrait +
            ", coverPage=" + coverPage +
            ", email=" + email +
            ", phone number=" + mobilePhoneNumber +
            ", brief_intro='" + brief_intro + '\'' +
            '}'
    }

    companion object {
        //region static util
        /**
         * 验证手机号
         */
        @JvmStatic
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
        @JvmStatic
        fun isEmail(email: String): Boolean {
            if (TextUtils.isEmpty(email)) {
                return false
            }
            val str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$"
            val p = Pattern.compile(str)
            return p.matcher(email).matches()
        } //endregion
    }
}