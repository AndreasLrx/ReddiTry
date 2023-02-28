package com.redditry.redditAPI

import android.content.SharedPreferences
import com.redditry.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object API {
    const val redirectUri = BuildConfig.REDDIT_REDIRECT_URI
    const val registerURL = "https://www.reddit.com/register/"

    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.reddit.com/api/v1/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
    var accessToken: String? = null
    var refreshToken: String? = null

    init {
    }

    fun loadFromPreferences(preferences: SharedPreferences) {
        accessToken = preferences.getString("redditToken", "")
        refreshToken = preferences.getString("redditRefreshToken", "")
    }

    fun createInstance(): RedditInterface {
        return retrofit.create(RedditInterface::class.java)
    }
}
