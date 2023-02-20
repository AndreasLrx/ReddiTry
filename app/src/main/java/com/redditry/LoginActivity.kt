package com.redditry

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kirkbushman.araw.helpers.AuthAppHelper
import com.kirkbushman.araw.helpers.AuthUserlessHelper
import com.redditry.databinding.LoginBinding
import com.redditry.utils.DoAsync
import com.redditry.utils.Provider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity:AppCompatActivity() {

    companion object {

        private const val PARAM_AUTO_LOGIN = "intent_param_auto_login"

        fun start(context: Context, stopAutoLogin: Boolean = false) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.putExtra(PARAM_AUTO_LOGIN, stopAutoLogin)

            context.startActivity(intent)
        }
    }

    lateinit var appAuth: AuthAppHelper

    lateinit var userlessAuth: AuthUserlessHelper

    private lateinit var binding:LoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appAuth = AuthAppHelper(
            context = this,
            clientId = "FpteTHr9cnd-5yViWFKp0w",
            redirectUrl = "http://localhost:3000/api/login",
            scopes = arrayOf("subscribe","structuredstyles","vote","mysubreddits","read","identity","account"), // array of scopes strings
            logging = true
        )

        userlessAuth = AuthUserlessHelper(this,"FpteTHr9cnd-5yViWFKp0w")

        binding.loginButton.setOnClickListener {
            binding.webv.visibility = View.VISIBLE
            if (!appAuth.shouldLogin()) {

                savedInstalledApp()
            } else {

                fetchInstalledApp()
            }
        }
    }

    private fun savedInstalledApp() {
        DoAsync(
            doWork = {
                val client = appAuth.getSavedRedditClient()
                if (client != null) {
                    Provider.setRedditClient(client)
                }
            },
            onPost = {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
            }
        )
    }

    private fun fetchInstalledApp() {
        appAuth.startWebViewAuthentication(binding.webv) {
            binding.webv.stopLoading()

            DoAsync(
                doWork = {
                    if (!userlessAuth.shouldLogin()) {
                        userlessAuth.forceRevoke()
                    }

                    val client = appAuth.getRedditClient(it)
                    if (client != null) {
                        Provider.setRedditClient(client)
                    }


                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            )
        }
    }

}