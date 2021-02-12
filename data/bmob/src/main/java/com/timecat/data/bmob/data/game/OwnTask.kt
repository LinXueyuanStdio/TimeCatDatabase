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
@AVClassName("OwnTask")
class OwnTask : AVObject("OwnTask"), Parcelable, Serializable {

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
    var task: Block
        get() = getAVObject("task")
        set(value) {
            put("task", value)
        }
    var receive: Boolean
        get() = getBoolean("receive")
        set(value) {
            put("receive", value)
        }

    //region Parcelable
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(toJSONString())
    }

    companion object CREATOR : Parcelable.Creator<OwnTask> {
        override fun createFromParcel(parcel: Parcel): OwnTask {
            val jsonString = parcel.readString()
            val rawObject = parseAVObject(jsonString)
            return Transformer.transform(rawObject, OwnTask::class.java)
        }

        override fun newArray(size: Int): Array<OwnTask?> {
            return arrayOfNulls(size)
        }
    }
    //endregion
}