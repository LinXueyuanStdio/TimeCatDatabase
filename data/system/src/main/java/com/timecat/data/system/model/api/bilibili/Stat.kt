package com.timecat.data.system.model.api.bilibili

data class Stat(
    val aid: Int,
    val coin: Int,
    val danmaku: Int,
    val dislike: Int,
    val favorite: Int,
    val his_rank: Int,
    val like: Int,
    val now_rank: Int,
    val reply: Int,
    val share: Int,
    val view: Int
)