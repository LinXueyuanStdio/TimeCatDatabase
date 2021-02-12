package com.timecat.data.bmob.data.game

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
@AVClassName("OwnItem")
class OwnItem: AVObject("OwnItem"), Parcelable, Serializable {

    var user: User
        get() = User.transform(getAVObject("user"))
        set(value) {
            put("user", value)
        }
    var item: Block
        get() = getAVObject("item")
        set(value) {
            put("item", value)
        }
    var count: Int
        get() = getInt("count")
        set(value) {
            put("count", value)
        }

    //region Parcelable
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(toJSONString())
    }

    companion object CREATOR : Parcelable.Creator<OwnItem> {
        override fun createFromParcel(parcel: Parcel): OwnItem {
            val jsonString = parcel.readString()
            val rawObject = parseAVObject(jsonString)
            return Transformer.transform(rawObject, OwnItem::class.java)
        }

        override fun newArray(size: Int): Array<OwnItem?> {
            return arrayOfNulls(size)
        }
    }
    //endregion
}