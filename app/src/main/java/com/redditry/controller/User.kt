package com.redditry.controller

import com.redditry.redditAPI.API
import com.redditry.redditAPI.MyProfilResponse
import com.redditry.redditAPI.MySubredditsResponse
import com.redditry.redditAPI.Post
import com.redditry.redditAPI.Pref
import com.redditry.redditAPI.S3Response
import com.redditry.redditAPI.SubredditAboutResponse
import com.redditry.redditAPI.SubredditEditData
import okhttp3.MultipartBody

class User {

    val reddit = API.createInstance()
    val s3 = API.createS3Instance()

    fun getMyProfil(): MyProfilResponse? {
        return reddit.getMyProfil().execute().body()
    }

    fun getMyPost(
        username: String,
        after: String = "",
        limit: Int = 10
    ): Pair<ArrayList<Post>, String?> {
        val posts = reddit.getMyPost(username, after, limit).execute().body()

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

    fun getPref(): Pref? {
        return reddit.getCountry().execute().body()
    }

    fun setCountry(pref: Pref): Pref? {
        return reddit.setCountry(pref).execute().body()
    }

    fun getEditSubreddit(subredditName: String): SubredditAboutResponse? {
        return reddit.getEditSubreddit(subredditName).execute().body()
    }

    fun updateSubreddit(subredditEditData: SubredditEditData) {
        reddit.updateSubreddit(
            subredditEditData.public_description,
            subredditEditData.over_18.toString(),
            subredditEditData.link_type,
            subredditEditData.type,
            subredditEditData.sr,
            subredditEditData.subreddit_id,
            subredditEditData.title
        ).execute().body()
    }

    fun getS3Bucket(
        profilName: String,
        filepath: String,
        imageType: String,
        mimetype: String
    ): S3Response? {
        return reddit.getS3Bucket(
            profilname = profilName,
            filepath = filepath,
            imagetype = imageType,
            mimetype = mimetype
        ).execute().body()
    }

    fun uploadToS3(url: String, parameters: Map<String, String>, file: MultipartBody.Part): String {
        val response = s3.uploadToS3(url, parameters, file).execute().body()
        val string = response?.string()

        // We want the url inside the Location markups
        val firstIndex = string?.indexOf("<Location>")?.plus(10)
        val secondIndex = string?.indexOf("</Location>")

        return string?.substring(firstIndex!!, secondIndex!!).toString()
    }

    fun changeProfileBanner(profilName: String, url: String) {
        reddit.changeProfileBanner(
            profilname = profilName,
            profileBanner = url
        ).execute().errorBody()?.string()
    }

    fun changeProfileIcon(profilName: String, url: String) {
        reddit.changeProfileIcon(
            profilname = profilName,
            profileIcon = url
        ).execute().errorBody()?.string()
    }
}
