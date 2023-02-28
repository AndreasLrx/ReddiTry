package com.redditry.redditAPI

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MyProfilResponse(
    @Json(name = "name")
    val name: String,
    @Json(name="icon_img")
    val icon_img: String?,
    @Json(name = "subreddit")
    val subreddit:Subreddit
)

@JsonClass(generateAdapter = true)
data class Subreddit(
    @Json(name = "banner_img")
    val banner_img: String?,
    @Json(name = "accept_followers")
    val isAcceptingFollowers: Boolean,
    @Json(name = "subscribers")
    val subscribers: Int,
    @Json(name = "public_description")
    val description: String?
)