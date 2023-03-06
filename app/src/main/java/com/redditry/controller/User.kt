package com.redditry.controller

import com.redditry.redditAPI.API
import com.redditry.redditAPI.MyPostResponse
import com.redditry.redditAPI.MyProfilResponse
import com.redditry.redditAPI.MySubredditsResponse

class User {

    val reddit = API.createInstance()

    fun getMyProfil(): MyProfilResponse? {
        return reddit.getMyProfil().execute().body()
    }

    fun getMyPost(): MyPostResponse? {
        return reddit.getMyPost().execute().body()
    }

    fun getMySubscribedSubreddits(): MySubredditsResponse? {
        return reddit.getMySubreddits().execute().body()
    }
}
