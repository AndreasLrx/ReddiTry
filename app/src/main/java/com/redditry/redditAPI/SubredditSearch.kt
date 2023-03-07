package com.redditry.redditAPI

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class SearchSubredditResponse(
    val data: SubredditNames
)


@JsonClass(generateAdapter = true)
data class SubredditNames(
    val title: String,
    val subreddit_id: String,
    val subreddit_type: String

)
