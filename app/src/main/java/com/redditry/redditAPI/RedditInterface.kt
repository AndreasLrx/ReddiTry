package com.redditry.redditAPI

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RedditInterface {
    @POST("access_token")
    @FormUrlEncoded
    fun login(
        @Header("Authorization") authorization: String,
        @Field("grant_type") grant_type: String = "authorization_code",
        @Field("code") code: String,
        @Field("redirect_uri") redirect_uri: String
    ): Call<TokenResponse>

    @GET("api/v1/me")
    fun getMyProfil(): Call<MyProfilResponse>

    @GET("user/{username}/submitted")
    fun getMyPost(
        @Path(
            value = "username",
            encoded = true
        ) username: String,
        @Query("after") after: String = "",
        @Query("limit") limit: Int = 25
    ): Call<PostList>

    @GET("subreddits/mine/subscriber")
    fun getMySubreddits(): Call<MySubredditsResponse>

    @GET("user/{username}/about")
    fun getUser(
        @Path(
            value = "username",
            encoded = true
        ) username: String
    ): Call<UserAboutResponse>

    @GET("{sort_by}")
    fun getPosts(
        @Path(
            value = "sort_by",
            encoded = true
        ) sort_by: String,
        @Query("after") after: String = "",
        @Query("limit") limit: Int = 25
    ): Call<PostList>

    @GET("{subreddit}/{sort_by}")
    fun getPostsFrom(
        @Path(
            value = "subreddit",
            encoded = true
        ) subreddit: String,
        @Path(
            value = "sort_by",
            encoded = true
        ) sort_by: String,
        @Query("after") after: String = "",
        @Query("limit") limit: Int = 25
    ): Call<PostList>

    @GET("{subredditName}/about")
    fun getSubreddit(
        @Path(
            value = "subredditName",
            encoded = false
        ) subreddit: String
    ): Call<SubredditAboutResponse>

    @GET("subreddits/search")
    fun searchSubreddit(
        @Query(value = "q") query: String,
        @Query(value = "show_users") showUsers: Boolean = false
    ): Call<MySubredditsResponse>

    @POST("api/site_admin?api_type=json")
    @FormUrlEncoded
    fun updateSubreddit(subredditEditRequest: SubredditEditRequest): Call<Void>

    @GET("api/search_reddit_names")
    fun searchSubreddit(
        @Query("query") query: String,
        @Query("exact") exact: Boolean = false,
        @Query("include_over_18") include_over_18: Boolean = true,
        @Query("include_unadvertisable") include_unadvertisable: Boolean = true
    ): Call<SearchSubredditResponse>

    @POST("api/subscribe")
    @FormUrlEncoded
    fun subscribe(
        @Field("action") action: String,
        @Field("sr") sr: String,
        @Field("skip_initial_defaults") skip_initial_defaults: Boolean = true
    ): Call<Void>

    @POST("api/subscribe")
    @FormUrlEncoded
    fun unsubscribe(
        @Field("action") action: String,
        @Field("sr") sr: String,
    ): Call<Void>

    @POST("api/vote")
    @FormUrlEncoded
    fun vote(
        @Field("dir") dir: Int,
        @Field("id") id: String
    ): Call<Void>
    
    @GET("r/{subredditName}/about/edit")
    fun getEditSubreddit(
        @Path(
            value = "user_id",
            encoded = true
        ) userId: String
    ): Call<SubredditAboutResponse>

    @POST("api/site_admin?api_type=json")
    @FormUrlEncoded
    fun updateSubreddit(
        @Field(value = "public_description") public_description: String,
        @Field(value = "over_18") over_18: String,
        @Field(value = "link_type") link_type: String,
        @Field(value = "type") type: String,
        @Field(value = "sr") sr: String,
        @Field(value = "subreddit_id") subreddit_id: String,
        @Field(value = "title") title: String
    ): Call<Void>

    @GET("api/v1/me/prefs")
    fun getCountry(): Call<Pref>

    @PATCH("api/v1/me/prefs")
    fun setCountry(@Body pref: Pref): Call<Pref>
}
