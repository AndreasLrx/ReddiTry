package com.redditry.redditAPI

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubredditAboutResponse(
    val data: Subreddit
)

@JsonClass(generateAdapter = true)
data class Subreddit(
    @Json(name = "banner_img")
    val banner_img: String?,
    @Json(name = "mobile_banner_image")
    val mobile_banner_img: String?,
    @Json(name = "accept_followers")
    val isAcceptingFollowers: Boolean,
    @Json(name = "subscribers")
    val subscribers: Int,
    @Json(name = "public_description")
    val description: String?,
    @Json(name = "display_name_prefixed")
    val displayName: String?,
    @Json(name = "icon_img")
    val icon_img: String?,
    val over18: Boolean,
    val title: String,
    val lang: String,
    val id: String
)
