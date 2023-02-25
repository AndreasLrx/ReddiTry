package com.redditry

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.redditry.databinding.LoginBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Credentials
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.IOException


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginBinding
    private var client = OkHttpClient()
    private var gson = Gson()
    private val redirectUri = "http://localhost:3000/api/login"
    private val clientId = "s-w2UUr5ZeLMbnI7lOMmwg"


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
            val params = uri.encodedQuery
            val code = params?.split("=")?.get(2)

            val formBody: RequestBody = FormBody.Builder()
                    .add("grant_type","authorization_code")
                    .add("code", code!!)
                    .add("redirect_uri", redirectUri)
                    .build()
            val credential: String = Credentials.basic(clientId, "")

            val request = Request.Builder()
                    .url("https://www.reddit.com/api/v1/access_token")
                    .addHeader("Authorization",credential)
                    .post(formBody)
                    .build()

            try {

                val response = client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        response.use {
                            if (!response.isSuccessful) throw IOException("Unexpected code $response")

                            val redditTokenResponse = gson.fromJson(response.body?.string(), RedditTokenResponse::class.java)
                            val myEdit = sharedPreferences.edit()
                            myEdit.putString("redditToken", redditTokenResponse.access_token)
                            myEdit.apply()

                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }
                })


            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        if (token != "" && token != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        binding.loginButton.setOnClickListener {
            val scope = "identity"
            val urlString = "https://www.reddit.com/api/v1/authorize.compact?client_id=$clientId&response_type=code&state=%22random%22&redirect_uri=$redirectUri&duration=permanent&scope=$scope"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlString))
            startActivity(intent)
        }
    }
}

class RedditTokenResponse (
    val access_token:String,
    val token_type:String,
    val expires_in:String,
    val scope:String,
    val refresh_token:String
)