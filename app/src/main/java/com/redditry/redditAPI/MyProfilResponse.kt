package com.redditry.redditAPI

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MyProfilResponse(
    @Json(name = "name")
    val name: String,
    @Json(name = "icon_img")
    val icon_img: String?,
    @Json(name = "subreddit")
    val subreddit: Subreddit
)
