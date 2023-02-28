package com.redditry.redditAPI

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

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
    fun getMyProfil():Call<MyProfilResponse>
}
