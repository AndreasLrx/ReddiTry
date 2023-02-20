package com.redditry.controller

import android.R
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import org.json.JSONException
import com.kirkbushman.araw.helpers.AuthAppHelper

class UserController {


    fun login(context: Context, webView: WebView, view: View){
        // step 1 - crete the help
        val helper = AuthAppHelper(
            context = context,
            clientId = "FpteTHr9cnd-5yViWFKp0w",
            redirectUrl = "http://localhost:3000/api/login",
            scopes = arrayOf("subscribe","structuredstyles","vote","mysubreddits","read","identity","account"), // array of scopes strings
            logging = true
        )

        // use shouldLogin() to check whether authentication already happened
        if (!helper.shouldLogin()) {
            // use saved one
        } else {
            // you must authenticate
            /*helper.startWebViewAuthentication(binding.browser) {
                binding.browser.stopLoading()

                DoAsync(
                    doWork = {
                        if (!userlessAuth.shouldLogin()) {
                            userlessAuth.forceRevoke()
                        }

                        val client = appAuth.getRedditClient(it)
                        if (client != null) {
                            Provider.setRedditClient(client)
                        }

                        checkAuthStatus()

                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                )
            }*/
        }

        // step 2 - obtain a client
        val client = helper.getRedditClient()
    }

}