package com.redditry.controller

import com.redditry.redditAPI.API

class Post {
    enum class SortBy {
        new, rising, hot, best
    }

    val reddit = API.createInstance()

    fun getPosts(
        where: SortBy = SortBy.new,
        after: String? = null,
        limit: Int = 10
    ): Pair<ArrayList<com.redditry.redditAPI.Post>, String?> {
        return getPostsFrom(null, where, after, limit)
    }

    fun getPostsFrom(
        subreddit: String? = null,
        where: SortBy = SortBy.new,
        after: String? = null,
        limit: Int = 10
    ): Pair<ArrayList<com.redditry.redditAPI.Post>, String?> {
        val posts = if (subreddit != null)
            reddit.getPostsFrom(subreddit, where.toString(), after ?: "", limit).execute()
                .body()
        else
            reddit.getPosts(where.toString(), after ?: "", limit).execute().body()

        val res = ArrayList<com.redditry.redditAPI.Post>()
        posts?.data?.children?.forEach {
            res.add(it.data)
        }
        return Pair(res, posts?.data?.after)
    }
}
