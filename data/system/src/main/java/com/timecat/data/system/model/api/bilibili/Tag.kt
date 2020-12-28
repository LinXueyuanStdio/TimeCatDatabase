package com.timecat.data.system.model.api.bilibili

data class Tag(
    val attribute: Int,
    val cover: String,
    val hated: Int,
    val hates: Int,
    val liked: Int,
    val likes: Int,
    val tag_id: Int,
    val tag_name: String
)