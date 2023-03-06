package com.redditry.redditAPI

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MySubredditsResponse(
    val data: MySubredditsDataResponse
)

@JsonClass(generateAdapter = true)
data class MySubredditsDataResponse(
    @Json(name = "dist")
    val numberOfPost: Int,
    val children: Array<SubredditData>
)

@JsonClass(generateAdapter = true)
data class SubredditData(
    val data: Subreddit
)
