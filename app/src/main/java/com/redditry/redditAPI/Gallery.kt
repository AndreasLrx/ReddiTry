package com.redditry.redditAPI

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Gallery(
    val items: Array<GalleryItem>?
)

@JsonClass(generateAdapter = true)
data class GalleryItem(
    val media_id: String,
    val id: Int
)
