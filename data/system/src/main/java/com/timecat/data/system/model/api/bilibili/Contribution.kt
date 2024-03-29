package com.timecat.data.system.model.api.bilibili

data class Contribution(
        val aid: Int,
        val ctime: Int,
        val owner: Owner,
        val pic: String,
        val stat: Stat,
        val title: String
)