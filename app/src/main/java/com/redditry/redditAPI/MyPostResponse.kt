package com.redditry.redditAPI

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MyPostResponse(
    val data:MyPostDataResponse
)

@JsonClass(generateAdapter = true)
data class MyPostDataResponse(
    @Json(name = "dist")
    val numberOfPost: Int,
    val children: Array<Post>
)

@JsonClass(generateAdapter = true)
data class Post(
    val data: PostData
)

@JsonClass(generateAdapter = true)
data class PostData(
    val title: String,
    @Json(name = "selftext")
    val content: String,
    val subreddit_name_prefixed:String,
    @Json(name = "ups")
    val voteUp: Int,
    @Json(name = "num_comments")
    val numComments:Int,
    val author: String
)