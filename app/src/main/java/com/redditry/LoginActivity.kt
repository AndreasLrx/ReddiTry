package com.redditry

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.redditry.databinding.LoginBinding


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginBinding

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
        var token = sharedPreferences.getString("redditToken", "")
        val uri = intent.data
        if (uri != null) {
            val params = uri.encodedQuery
            token = params?.split("=")?.get(2)
            val myEdit = sharedPreferences.edit()
            myEdit.putString("redditToken", token.toString())
            myEdit.apply()
            binding.loginRegister.text = token
        }
        if (token != "" && token != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            binding.loginButton.setOnClickListener {
                val redirectUri = "http://localhost:3000/api/login"
                val clientId = "s-w2UUr5ZeLMbnI7lOMmwg"
                val scope = "identity"
                val urlString = "https://www.reddit.com/api/v1/authorize.compact?client_id=$clientId&response_type=code&state=%22random%22&redirect_uri=$redirectUri&duration=permanent&scope=$scope"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlString))
                startActivity(intent)
            }
        }
    }
}