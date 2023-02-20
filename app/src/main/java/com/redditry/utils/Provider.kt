package com.redditry.utils

import android.content.Context
import com.kirkbushman.araw.RedditClient
import com.kirkbushman.araw.helpers.AuthAppHelper
import com.kirkbushman.araw.helpers.AuthUserlessHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Provider {

    @Volatile
    private var mRedditClient: RedditClient? = null

    @Provides
    @Singleton
    fun provideAppAuthHelper(
        @ApplicationContext context: Context,
    ): AuthAppHelper {

        return AuthAppHelper(
            context = context,
            clientId = "FpteTHr9cnd-5yViWFKp0w",
            redirectUrl = "http://localhost:3000/api/login",
            scopes = arrayOf("subscribe","structuredstyles","vote","mysubreddits","read","identity","account"), // array of scopes strings
            logging = true,
            //disableLegacyEncoding = disableLegacyEncoding
        )
    }

    @JvmStatic
    @Synchronized
    fun setRedditClient(client: RedditClient) {
        return synchronized(this) {

            if (mRedditClient == null) {
                mRedditClient = client
            }
        }
    }

    @Provides
    @Singleton
    fun provideRedditClient(

        appAuth: AuthAppHelper,
        userlessAuth: AuthUserlessHelper

    ): RedditClient {

        if (mRedditClient == null) {

            // check if there is a registered token
            // with username and password
            if (appAuth.hasSavedBearer()) {
                mRedditClient = appAuth.getSavedRedditClient()
            }

            // if there is no token, verify if there is a userless token
            if (userlessAuth.hasSavedBearer()) {
                mRedditClient = userlessAuth.getSavedRedditClient()
            }
        }

        return mRedditClient!!
    }
}
