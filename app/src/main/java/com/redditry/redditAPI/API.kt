package com.redditry.redditAPI

import JSONObjectAdapter
import android.content.SharedPreferences
import com.redditry.BuildConfig
import com.redditry.utils.ToStringConverterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object API {
    const val redirectUri = BuildConfig.REDDIT_REDIRECT_URI
    const val registerURL = "https://www.reddit.com/register/"

    private val moshi =
        Moshi.Builder().addLast(KotlinJsonAdapterFactory()).add(JSONObjectAdapter()).build()
    private val retrofitLogin = Retrofit.Builder()
        .baseUrl("https://www.reddit.com/api/v1/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
    private val retrofitS3 = Retrofit.Builder()
        .baseUrl("https://www.reddit.com")
        .addConverterFactory(ToStringConverterFactory())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
    private lateinit var retrofit: Retrofit
    var accessToken: String? = null
    var refreshToken: String? = null

    init {
    }

    // set the access token and add the authorization header to the retrofit client
    @JvmName("set_access_token")
    fun setAccessToken(accessToken: String?) {
        this.accessToken = accessToken

        if (accessToken != null) {
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor {
                val original: Request = it.request()
                val originalHttpUrl = original.url()
                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("raw_json", "1")
                    .build()

                val request = original.newBuilder().url(url)
                    .header("Authorization", "Bearer " + API.accessToken)
                    .method(original.method(), original.body())
                    .build()
                return@addInterceptor it.proceed(request)
            }
            // create the retrofit client
            retrofit = Retrofit.Builder()
                .baseUrl("https://oauth.reddit.com/")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .client(httpClient.build())
                .build()
        }
    }

    fun loadFromPreferences(preferences: SharedPreferences) {
        setAccessToken(preferences.getString("redditToken", ""))
        refreshToken = preferences.getString("redditRefreshToken", "")
    }

    fun createInstance(): RedditInterface {
        return retrofit.create(RedditInterface::class.java)
    }

    fun createInstanceLogin(): RedditInterface {
        return retrofitLogin.create(RedditInterface::class.java)
    }

    fun createS3Instance(): RedditInterface {
        return retrofitS3.create(RedditInterface::class.java)
    }
}
