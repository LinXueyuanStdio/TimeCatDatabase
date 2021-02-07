package com.timecat.data.bmob.data.game

import android.os.Parcel
import android.os.Parcelable
import cn.leancloud.AVObject
import cn.leancloud.Transformer
import cn.leancloud.annotation.AVClassName
import cn.leancloud.types.AVDate
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Block
import org.joda.time.DateTime
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/15
 * @description 用户拥有物品
 * @usage null
 */
@AVClassName("OwnActivity")
class OwnActivity(
    user: User,
    activity: Block,
    activeTime: AVDate = AVDate(DateTime().toString(AVDate.DEFAULT_FORMAT)),
    expireTime: AVDate = AVDate(DateTime().toString(AVDate.DEFAULT_FORMAT)),
) : AVObject("OwnActivity"), Parcelable, Serializable {

    var activeDateTime: DateTime
        get() = DateTime(activeTime.date)
        set(value) {
            activeTime = AVDate(value.toString(AVDate.DEFAULT_FORMAT))
        }
    var expireDateTime: DateTime
        get() = DateTime(expireTime.date)
        set(value) {
            expireTime = AVDate(value.toString(AVDate.DEFAULT_FORMAT))
        }
    var user: User
        get() = User.transform(getAVObject("user"))
        set(value) {
            put("user", value)
        }
    var activity: Block
        get() = getAVObject("activity")
        set(value) {
            put("activity", value)
        }
    var activeTime: AVDate
        get() = get("activeTime") as AVDate
        set(value) {
            put("activeTime", value)
        }
    var expireTime: AVDate
        get() = get("expireTime") as AVDate
        set(value) {
            put("expireTime", value)
        }

    init {
        this.user = user
        this.activity = activity
        this.activeTime = activeTime
        this.expireTime = expireTime
    }
    constructor() : this(User(), Block())

    //region Parcelable
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(toJSONString())
    }

    companion object CREATOR : Parcelable.Creator<OwnActivity> {
        override fun createFromParcel(parcel: Parcel): OwnActivity {
            val jsonString = parcel.readString()
            val rawObject = parseAVObject(jsonString)
            return Transformer.transform(rawObject, OwnActivity::class.java)
        }

        override fun newArray(size: Int): Array<OwnActivity?> {
            return arrayOfNulls(size)
        }
    }
    //endregion
}