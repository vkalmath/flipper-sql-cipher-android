package com.vk.flipperapp

import android.app.Application
import com.facebook.flipper.BuildConfig
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.crashreporter.CrashReporterPlugin
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.navigation.NavigationFlipperPlugin
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin
import com.facebook.soloader.SoLoader
import com.flipper.sqlcipher.SqlCipherDatabasesFlipperPlugin


class DemoApp : Application() {

    override fun onCreate() {
        super.onCreate()
        SoLoader.init(this, false)

        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
            val client = AndroidFlipperClient.getInstance(applicationContext)
            client.addPlugin(InspectorFlipperPlugin(applicationContext, DescriptorMapping.withDefaults()))
            client.addPlugin(SqlCipherDatabasesFlipperPlugin(applicationContext, "DB_ENCRYPTION_PWD", listOf("DB_NAMES")))
            client.addPlugin(DatabasesFlipperPlugin(applicationContext))

            client.addPlugin(
                SharedPreferencesFlipperPlugin(
                    applicationContext
                )
            )
            client.addPlugin(CrashReporterPlugin.getInstance())
            client.addPlugin(NavigationFlipperPlugin.getInstance())
            client.start()
        }
    }
}