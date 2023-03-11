package com.redditry.redditAPI

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class S3Response(
    val s3UploadLease: S3UploadLease
)

@JsonClass(generateAdapter = true)
data class S3UploadLease(
    val action: String,
    val fields: List<Field>
)

@JsonClass(generateAdapter = true)
data class Field(
    val name: String,
    val value: String
)
