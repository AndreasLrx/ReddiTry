package com.redditry.controller

import com.redditry.redditAPI.API

class Post {
    enum class SortBy {
        rising, new, hot, best
    }

    val reddit = API.createInstance()

    fun getPosts(
        where: SortBy = SortBy.new,
        after: String? = null,
        limit: Int = 25
    ): Pair<ArrayList<com.redditry.redditAPI.Post>, String?> {
        val posts = reddit.getPosts(where.toString(), after ?: "", limit).execute().body()
        val res = ArrayList<com.redditry.redditAPI.Post>()
        posts?.data?.children?.forEach {
            res.add(it.data)
        }
        return Pair(res, posts?.data?.after)
    }
}
