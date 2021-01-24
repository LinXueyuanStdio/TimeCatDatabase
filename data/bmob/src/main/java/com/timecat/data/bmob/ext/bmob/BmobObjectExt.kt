package com.timecat.data.bmob.ext.bmob

import cn.leancloud.AVObject
import com.jess.arms.utils.LogUtils
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.*
import com.timecat.data.bmob.ext.toDataError
import io.reactivex.disposables.Disposable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description null
 * @usage null
 */
open class Saver<T : AVObject> : SimpleRequestCallback<T>() {
    lateinit var target: T
    fun build(): Disposable {
        return target.saveInBackground().subscribe({
            LogUtils.debugInfo("onNext $it")
            onSuccess(it as T)
        }, {
            LogUtils.debugInfo("onError $it")
            onError(it.toDataError())
        }, {
            LogUtils.debugInfo("onComplete")
        })
    }
}

fun saveBlock(create: Saver<Block>.() -> Unit) = save(create)
fun saveBlockRelation(create: Saver<Block2Block>.() -> Unit) = save(create)
fun saveUser(create: Saver<User>.() -> Unit) = save(create)
fun saveUserRelation(create: Saver<User2User>.() -> Unit) = save(create)
fun saveAction(create: Saver<Action>.() -> Unit) = save(create)
fun saveInterAction(create: Saver<InterAction>.() -> Unit) = save(create)
fun <T : AVObject> save(create: Saver<T>.() -> Unit) = Saver<T>().apply(create).also { it.build() }


class Deleter<T : AVObject> : SimpleRequestCallback<T>() {
    lateinit var target: T
    fun build(): Disposable {
        return target.deleteInBackground().subscribe({
            LogUtils.debugInfo("onNext $it")
            onSuccess(target)
        }, {
            LogUtils.debugInfo("onError $it")
            onError(it.toDataError())
        }, {
            LogUtils.debugInfo("onComplete")
        })
    }
}

fun deleteBlock(create: Deleter<Block>.() -> Unit) = delete(create)
fun deleteBlockRelation(create: Deleter<Block2Block>.() -> Unit) = delete(create)
fun deleteUser(create: Deleter<User>.() -> Unit) = delete(create)
fun deleteUserRelation(create: Deleter<User2User>.() -> Unit) = delete(create)
fun deleteAction(create: Deleter<Action>.() -> Unit) = delete(create)
fun deleteInterAction(create: Deleter<InterAction>.() -> Unit) = delete(create)
fun <T : AVObject> delete(create: Deleter<T>.() -> Unit) = Deleter<T>().apply(create).also { it.build() }

class Updater<T : AVObject> : Saver<T>()

fun updateBlock(create: Updater<Block>.() -> Unit) = update(create)
fun updateUser(create: Updater<User>.() -> Unit) = update(create)
fun <T : AVObject> update(create: Updater<T>.() -> Unit) = Updater<T>().apply(create).also { it.build() }


fun saveBatch(create: BatchSaver.() -> Unit) = BatchSaver().apply(create).also { it.build() }
class BatchSaver : RequestListCallback<AVObject>() {
    lateinit var target: List<AVObject>
    fun build(): Disposable {
        return AVObject.saveAllInBackground(target)
            .subscribe({
                LogUtils.debugInfo("onNext $it")
                onSuccess(target)
            }, {
                LogUtils.debugInfo("onError $it")
                onError(it.toDataError())
            }, {
                LogUtils.debugInfo("onComplete")
            })
    }
}

fun deleteBatch(create: BatchDeleter.() -> Unit) = BatchDeleter().apply(create).also { it.build() }
class BatchDeleter : RequestListCallback<AVObject>() {
    lateinit var target: List<AVObject>
    fun build(): Disposable {
        return AVObject.deleteAllInBackground(target)
            .subscribe({
                LogUtils.debugInfo("onNext $it")
                onSuccess(target)
            }, {
                LogUtils.debugInfo("onError $it")
                onError(it.toDataError())
            }, {
                LogUtils.debugInfo("onComplete")
            })
    }
}
