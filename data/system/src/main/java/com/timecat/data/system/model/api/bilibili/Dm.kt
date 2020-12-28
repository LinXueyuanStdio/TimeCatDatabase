package com.timecat.data.system.model.api.bilibili

data class Dm(
        val closed: Boolean,
        val count: Int,
        val mask: Mask,
        val real_name: Boolean
)