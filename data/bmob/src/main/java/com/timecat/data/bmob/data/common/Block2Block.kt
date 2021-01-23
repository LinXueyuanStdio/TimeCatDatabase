package com.timecat.data.bmob.data.common

import cn.leancloud.AVObject
import cn.leancloud.annotation.AVClassName
import com.timecat.data.bmob.data._User
import com.timecat.identity.data.action.InterActionType
import com.timecat.identity.data.block_block.Block2BlockType
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-09
 * @description null
 * @usage null
 */
@AVClassName("Block2Block")
class Block2Block(
    user: _User,
    from: Block,
    to: Block,
    @Block2BlockType
    type: Int = 0,
    structure: String = "",
    status: Long = 0
) : AVObject("Block2Block"), Serializable {
    //region field
    var user: _User
        get() = getAVObject("user")
        set(value) {
            put("user", value)
        }

    var from: Block
        get() = getAVObject("from")
        set(value) {
            put("from", value)
        }
    var to: Block
        get() = getAVObject("to")
        set(value) {
            put("to", value)
        }
    var type: Int
        get() = getInt("type")
        set(value) {
            put("type", value)
        }
    var structure: String
        get() = getString("structure")
        set(value) {
            put("structure", value)
        }
    var status: Long
        get() = getLong("status")
        set(value) {
            put("status", value)
        }

    init {
        this.user = user
        this.from = from
        this.to = to
        this.type = type
        this.structure = structure
        this.status = status
    }
    //endregion

    override fun toString(): String {
        return "$objectId(type=$type, user=${user.nick}, structure='$structure', status=$status, \n" +
            "        from=$from," +
            "        to=$to)"
    }


}

