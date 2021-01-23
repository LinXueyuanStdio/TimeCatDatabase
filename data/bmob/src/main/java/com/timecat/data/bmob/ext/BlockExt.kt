package com.timecat.data.bmob.ext

import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.requestActionCount
import com.timecat.data.bmob.ext.bmob.saveBlock
import com.timecat.data.bmob.ext.bmob.updateBlock
import com.timecat.data.bmob.ext.net.allFans
import com.timecat.identity.data.service.DataError

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-16
 * @description null
 * @usage null
 */
fun Block.isUsed() = incrementAndUpdate("usedBy")

fun Block.isCommented(onDone: ((e: DataError?) -> Unit)? = null) =
    incrementAndUpdate("comments", onDone)

fun Block.isLiked(onDone: ((e: DataError?) -> Unit)? = null) =
    incrementAndUpdate("likes", onDone)

fun Block.isRelays(onDone: ((e: DataError?) -> Unit)? = null) =
    incrementAndUpdate("relays", onDone)

fun Block.isFollowed(onDone: ((e: DataError?) -> Unit)? = null) =
    incrementAndUpdate("followers", onDone)

fun Block.isUnCommented(onDone: ((e: DataError?) -> Unit)? = null) =
    decrementAndUpdate("comments", onDone)

fun Block.isUnLiked(onDone: ((e: DataError?) -> Unit)? = null) =
    decrementAndUpdate("likes", onDone)

fun Block.isUnRelays(onDone: ((e: DataError?) -> Unit)? = null) =
    decrementAndUpdate("relays", onDone)

fun Block.isUnFollowed(onDone: ((e: DataError?) -> Unit)? = null) =
    decrementAndUpdate("followers", onDone)

fun Block.incrementAndUpdate(key: String, onDone: ((e: DataError?) -> Unit)? = null) {
    if (!objectId.isNullOrBlank()) {
        className = "Block"
        increment(key)
        updateBlock {
            target = this@incrementAndUpdate
            onDone?.let { onError = it }
        }
    }
}

fun Block.decrementAndUpdate(key: String, onDone: ((e: DataError?) -> Unit)? = null) {
    if (!objectId.isNullOrBlank()) {
        className = "Block"
        increment(key, -1)
        saveBlock {
            target = this@decrementAndUpdate
            onDone?.let { onError = it }
        }
    }
}

fun Block.focusSum(onDaoCount: (Int) -> Unit) {
    requestActionCount {
        query = allFans()
        onSuccess = onDaoCount
    }
}

