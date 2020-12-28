package com.timecat.data.system.model.api.bilibili

data class ReqUser(
    val attention: Int,
    val coin: Int,
    val dislike: Int,
    val favorite: Int,
    val like: Int
)