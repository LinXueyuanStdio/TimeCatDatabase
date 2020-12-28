package com.timecat.data.system.model.api

import com.timecat.data.system.model.api.bilibili.Data

data class BiliBiliVideo(
        val code: Int,
        val data: Data,
        val message: String,
        val ttl: Int
)