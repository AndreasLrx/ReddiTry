package com.redditry.redditAPI

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubredditEditRequest(
    val data: SubredditEditData
)

@JsonClass(generateAdapter = true)
data class SubredditEditData(
    val title: String,
    val public_description: String,
    val over_18: Boolean,
    val link_type: String = "any",
    val type: String,
    val sr: String,
    val subreddit_id: String
)
