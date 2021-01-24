package com.timecat.data.bmob.data

import android.os.Parcel
import android.os.Parcelable
import cn.leancloud.AVFile
import cn.leancloud.AVUser
import cn.leancloud.Transformer
import cn.leancloud.annotation.AVClassName
import java.util.*

/**
 * Created by yc on 2018/2/2.
 */
@AVClassName("User")
class User() : AVUser(), Parcelable {
    //region getter and setter
    //个性
    //头像
    var headPortrait: AVFile?
        get() = getAVFile("avatar")
        set(value) {
            put("avatar", value)
        }

    //背景图
    var coverPage: AVFile?
        get() = getAVFile("cover")
        set(value) {
            put("cover", value)
        }

    //个性签名
    var intro: String
        get() = getString("intro") ?: "剑光纵横三万里，一剑光寒十九州"
        set(value) {
            put("intro", value)
        }
    var nickName: String
        get() = getString("nickName") ?: "薛定谔的喵"
        set(value) {
            put("nickName", value)
        }

    //个人信息
    var gender: String
        get() = getString("gender") ?: "未知"
        set(value) {
            put("gender", value)
        }
    var address: String
        get() = getString("address") ?: ""
        set(value) {
            put("address", value)
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
    var lastSettleTime: Date
        get() = getDate("lastSettleTime")
        set(value) {
            put("lastSettleTime", value)
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

    init {
        this.headPortrait = null
        this.coverPage = null
        this.intro = "剑光纵横三万里，一剑光寒十九州"
        this.nickName = "薛定谔的喵"
        this.gender = ""
        this.address = ""
        this.level = 0
        this.exp = 0
        this.star = 0
        this.water = 0
        this.lastSettleTime = Date()
        this.currency = 0
        this.charge = 0
    }

    constructor(objId: String?) : this() {
        setObjectId(objId)
    }

    val nick: String
        get() = nickName

    var avatar: String
        get() = if (headPortrait == null) {
            "https://lc-gluttony.s3.amazonaws.com/lVumM4aviuXn/fb9add291c586437b3de.png/ic_launcher.png"
        } else headPortrait!!.url
        set(avatar) {
            headPortrait = AVFile("avatar", avatar)
        }
    var cover: String
        get() = if (coverPage == null) {
            "http://d.hiphotos.baidu.com/zhidao/pic/item/bf096b63f6246b601ffeb44be9f81a4c510fa218.jpg"
        } else coverPage!!.url
        set(cover) {
            coverPage = AVFile("cover", cover)
        }

    constructor(parcel: Parcel) : this() {
    }

    //endregion

    //region Parcelable
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(toJSONString())
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            val jsonString = parcel.readString()
            val rawObject = parseAVObject(jsonString)
            return Transformer.transform(rawObject, User::class.java)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }

        @JvmStatic
        fun transform(user: AVUser): User {
            val jsonString = user.toJSONString()
            val rawObject = parseAVObject(jsonString)
            return Transformer.transform(rawObject, User::class.java)
        }
    }
    //endregion
}