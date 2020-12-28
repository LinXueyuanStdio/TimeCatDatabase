package com.timecat.data.system.model.api.bilibili

data class Cm(
        val ad_info: AdInfo,
        val client_ip: String,
        val index: Int,
        val is_ad_loc: Boolean,
        val request_id: String,
        val rsc_id: Int,
        val src_id: Int
)