package com.redditry.redditAPI

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubredditEditRequest(
    val data: SubredditEditData
)

@JsonClass(generateAdapter = true)
data class SubredditEditData(
    val public_description: String,
    val accept_followers: Boolean,
    val over_18: Boolean,
    val title: String,
    val link_type: String = "any",
    val type: String,
    val language: String,
    val sr: String,
    val subreddit_id: String
)
