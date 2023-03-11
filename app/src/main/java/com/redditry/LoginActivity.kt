package com.redditry

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.redditry.databinding.ActivityLoginBinding
import com.redditry.redditAPI.API
import okhttp3.Credentials

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    // on create inflate the layout and set the register button to open the register page
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.registerButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(API.registerURL))
            startActivity(intent)
        }

        // If the user is not logged in, try to login
        val sharedPreferences = getSharedPreferences("redditry", MODE_PRIVATE)
        API.loadFromPreferences(sharedPreferences)
        val uri = intent.data
        if (uri != null && API.accessToken == "") {
            startAnimation()
            val params = uri.encodedQuery
            val code = params?.split("=")?.get(2)

            val redditLogin = API.createInstanceLogin()

            // Login to reddit and save the token in the shared preferences
            Thread {
                val resp =
                    redditLogin
                        .login(
                            Credentials.basic(BuildConfig.REDDIT_CLIENT_ID, ""),
                            code = code!!,
                            redirect_uri = API.redirectUri
                        )
                        .execute()
                if (resp.isSuccessful) {
                    val myEdit = sharedPreferences.edit()
                    myEdit.putString("redditToken", resp.body()?.access_token)
                    myEdit.putString("redditRefreshToken", resp.body()?.refresh_token)
                    myEdit.apply()
                    API.loadFromPreferences(sharedPreferences)

                    val intent = Intent(this@LoginActivity, TutorialActivity::class.java)
                    startActivity(intent)
                }
                stopAnimation()
            }
                .start()
        }
        // If the user is already logged in, open the main activity
        if (API.accessToken != "" && API.accessToken != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            val scope = BuildConfig.REDDIT_SCOPE
            val urlString =
                "https://www.reddit.com/api/v1/authorize.compact?client_id=" +
                    BuildConfig.REDDIT_CLIENT_ID +
                    "&response_type=code&state=%22random%22&redirect_uri=" +
                    API.redirectUri +
                    "&duration=permanent&scope=$scope"
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
        binding.logo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate))
    }

    private fun stopAnimation() {
        binding.logo.clearAnimation()
    }
}
