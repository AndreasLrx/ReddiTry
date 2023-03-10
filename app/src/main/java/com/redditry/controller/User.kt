package com.redditry.controller

import com.redditry.redditAPI.API
import com.redditry.redditAPI.MyProfilResponse
import com.redditry.redditAPI.MySubredditsResponse
import com.redditry.redditAPI.Post

class User {

    val reddit = API.createInstance()

    fun getMyProfil(): MyProfilResponse? {
        return reddit.getMyProfil().execute().body()
    }

    fun getMyPost(
        username: String,
        after: String = "",
        limit: Int = 10
    ): Pair<ArrayList<Post>, String?> {
        val posts = reddit.getMyPost(username, after ?: "", limit).execute().body()

        val res = ArrayList<com.redditry.redditAPI.Post>()
        posts?.data?.children?.forEach {
            res.add(it.data)
        }
        return Pair(res, posts?.data?.after)
    }

    fun getMySubscribedSubreddits(): MySubredditsResponse? {
        return reddit.getMySubreddits().execute().body()
    }

    fun getUser(username: String): MyProfilResponse? {
        return reddit.getUser(username).execute().body()?.data
    }
}
