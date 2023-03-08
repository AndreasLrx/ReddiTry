package com.redditry.redditAPI

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.json.JSONObject

@JsonClass(generateAdapter = true)
data class PostList(
    val data: PostListData
)

@JsonClass(generateAdapter = true)
data class PostListData(
    val after: String?,
    @Json(name = "dist")
    val numberOfPost: Int,
    val children: Array<PostData>
)

@JsonClass(generateAdapter = true)
data class PostData(
    val data: Post
)

@JsonClass(generateAdapter = true)
data class Post(
    val title: String,
    @Json(name = "selftext")
    val content: String,
    val subreddit_name_prefixed: String,
    @Json(name = "ups")
    val voteUp: Int,
    @Json(name = "downs")
    val voteDown: Int,
    @Json(name = "num_comments")
    val numComments: Int,
    val author: String,
    @Json(name = "url_overridden_by_dest")
    val contentUrl: String?,
    val is_gallery: Boolean?,
    val is_video: Boolean?,
    val gallery_data: Gallery?,
    val media_metadata: JSONObject?
)
