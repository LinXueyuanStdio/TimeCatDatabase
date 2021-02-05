package com.timecat.data.bmob.data.mail

import android.os.Parcel
import android.os.Parcelable
import cn.leancloud.AVObject
import cn.leancloud.Transformer
import cn.leancloud.annotation.AVClassName
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Block
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/15
 * @description 用户拥有物品
 * @usage null
 */
@AVClassName("OwnMail")
class OwnMail(
    user: User,
    mail: Block,
    receive: Boolean = false,
) : AVObject("OwnMail"), Parcelable, Serializable {

    var user: User
        get() = User.transform(getAVObject("user"))
        set(value) {
            put("user", value)
        }
    var mail: Block
        get() = getAVObject("mail")
        set(value) {
            put("mail", value)
        }
    var receive: Boolean
        get() = getBoolean("receive")
        set(value) {
            put("receive", value)
        }

    init {
        this.user = user
        this.mail = mail
        this.receive = receive
    }

    constructor() : this(User(), Block())

    //region Parcelable
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(toJSONString())
    }

    companion object CREATOR : Parcelable.Creator<OwnMail> {
        override fun createFromParcel(parcel: Parcel): OwnMail {
            val jsonString = parcel.readString()
            val rawObject = parseAVObject(jsonString)
            return Transformer.transform(rawObject, OwnMail::class.java)
        }

        override fun newArray(size: Int): Array<OwnMail?> {
            return arrayOfNulls(size)
        }
    }
    //endregion

}