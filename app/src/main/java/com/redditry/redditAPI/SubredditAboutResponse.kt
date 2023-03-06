package com.redditry.redditAPI

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubredditAboutResponse(
    val data: SubredditAboutData
)

@JsonClass(generateAdapter = true)
data class SubredditAboutData(
    val public_description: String,
    val accept_followers: Boolean,
    val over_18: Boolean,
    val title: String,
    val language: String,
    val subreddit_id: String,
    val subreddit_type: String
)
