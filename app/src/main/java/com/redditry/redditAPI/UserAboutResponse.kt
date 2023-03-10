package com.redditry.redditAPI

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserAboutResponse(
    val data: MyProfilResponse
)
