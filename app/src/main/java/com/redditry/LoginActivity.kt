package com.redditry

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.redditry.databinding.LoginBinding
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Credentials
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginBinding
    private val redirectUri = BuildConfig.REDDIT_REDIRECT_URI
    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.reddit.com/api/v1/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loginRegister.setOnClickListener {
            val urlString = "https://www.reddit.com/register/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlString))
            startActivity(intent)
        }

        val sharedPreferences = getSharedPreferences("redditry", MODE_PRIVATE)
        val token = sharedPreferences.getString("redditToken", "")
        val uri = intent.data
        if (uri != null && token == "") {
            startAnimation()
            val params = uri.encodedQuery
            val code = params?.split("=")?.get(2)

            val api = retrofit.create(ApiReddit::class.java)

            GlobalScope.launch {
                val resp = api.login(
                    Credentials.basic(BuildConfig.REDDIT_CLIENT_ID, ""),
                    code = code!!,
                    redirect_uri = redirectUri
                )
                    .execute()
                if (resp.isSuccessful) {
                    val myEdit = sharedPreferences.edit()
                    myEdit.putString("redditToken", resp.body()?.access_token)
                    myEdit.apply()

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                }
                stopAnimation()
            }
        }
        if (token != "" && token != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        binding.loginButton.setOnClickListener {
            val scope = BuildConfig.REDDIT_SCOPE
            val urlString = "https://www.reddit.com/api/v1/authorize.compact?client_id=" + BuildConfig.REDDIT_CLIENT_ID + "&response_type=code&state=%22random%22&redirect_uri=$redirectUri&duration=permanent&scope=$scope"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlString))
            try {
                intent.setPackage("com.android.chrome")
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                intent.setPackage(null)
                startActivity(intent)
            }
        }
    }

    private fun startAnimation() {
        binding.loginLogo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate))
    }

    private fun stopAnimation() {
        binding.loginLogo.clearAnimation()
    }

    interface ApiReddit {
        @POST("access_token")
        @FormUrlEncoded
        fun login(
            @Header("Authorization") authorization: String,
            @Field("grant_type") grant_type: String = "authorization_code",
            @Field("code") code: String,
            @Field("redirect_uri") redirect_uri: String
        ): Call<RedditTokenResponse>
    }

    @JsonClass(generateAdapter = true)
    data class RedditTokenResponse(
        @Json(name = "access_token")
        val access_token: String?,
        @Json(name = "token_type")
        val token_type: String?,
        @Json(name = "expires_in")
        val expires_in: String?,
        @Json(name = "scope")
        val scope: String?,
        @Json(name = "refresh_token")
        val refresh_token: String?
    )
}
