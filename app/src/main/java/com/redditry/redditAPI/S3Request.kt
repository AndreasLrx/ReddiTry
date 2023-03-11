package com.redditry.redditAPI

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class S3Request(
    val filepath: String,
    val imagetype: String,
    val mimitype: String
)
