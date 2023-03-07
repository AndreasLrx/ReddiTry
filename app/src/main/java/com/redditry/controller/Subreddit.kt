package com.redditry.controller

import com.redditry.redditAPI.API
import com.redditry.redditAPI.MySubredditsResponse
import com.redditry.redditAPI.Subreddit

class Subreddit {
    val reddit = API.createInstance()

    fun getSubreddit(name: String): Subreddit? {
        return reddit.getSubreddit(name).execute().body()?.data
    }

    fun search(query: String, withUsers: Boolean = false): MySubredditsResponse? {
        return reddit.searchSubreddit(query, withUsers).execute().body()
    }
}
