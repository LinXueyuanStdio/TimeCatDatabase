package com.timecat.data.bmob.data.game

import android.os.Parcel
import android.os.Parcelable
import cn.leancloud.AVObject
import cn.leancloud.Transformer
import cn.leancloud.annotation.AVClassName
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.getUserObj
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/15
 * @description 用户 user 在商店 shop 用 pay 钱 money 购买物品 good 共 gain 个
 * @usage null
 */
@AVClassName("Pay")
class Pay : AVObject("Pay"), Parcelable, Serializable {

    var user: User
        get() = getUserObj()
        set(value) {
            put("user", value)
        }
    var shop: Block
        get() = getAVObject("shop")
        set(value) {
            put("shop", value)
        }
    var money: Block
        get() = getAVObject("money")
        set(value) {
            put("money", value)
        }
    var good: Block
        get() = getAVObject("good")
        set(value) {
            put("good", value)
        }
    var pay: Int
        get() = getInt("pay")
        set(value) {
            put("pay", value)
        }
    var gain: Int
        get() = getInt("gain")
        set(value) {
            put("gain", value)
        }

    //region Parcelable
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(toJSONString())
    }

    companion object CREATOR : Parcelable.Creator<Pay> {
        override fun createFromParcel(parcel: Parcel): Pay {
            val jsonString = parcel.readString()
            val rawObject = parseAVObject(jsonString)
            return Transformer.transform(rawObject, Pay::class.java)
        }

        override fun newArray(size: Int): Array<Pay?> {
            return arrayOfNulls(size)
        }
    }
    //endregion
}