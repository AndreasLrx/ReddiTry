package com.redditry.controller

import com.redditry.redditAPI.API

class Subreddit {
    val reddit = API.createInstance()

    fun getSubreddit(name: String): com.redditry.redditAPI.Subreddit? {
        val res = reddit.getSubreddit(name).execute().body()

        return res?.data
    }

}