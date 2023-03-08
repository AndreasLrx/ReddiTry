package com.redditry.controller

import com.redditry.redditAPI.API
import com.redditry.redditAPI.MySubredditsResponse
import com.redditry.redditAPI.Subreddit
import retrofit2.Response

class Subreddit {
    val reddit = API.createInstance()

    fun getSubreddit(name: String): Subreddit? {
        return reddit.getSubreddit(name).execute().body()?.data
    }

    fun search(query: String, withUsers: Boolean = false): MySubredditsResponse? {
        return reddit.searchSubreddit(query, withUsers).execute().body()
    }

    fun subscribe(name: String): Response<Void> {
        return reddit.subscribe("sub", name).execute()
    }

    fun unsubscribe(name: String): Response<Void> {
        return reddit.unsubscribe("unsub", name).execute()
    }
}
