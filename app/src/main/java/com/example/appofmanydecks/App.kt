package com.example.appofmanydecks

import android.app.Application
import logcat.AndroidLogcatLogger
import logcat.LogPriority
import logcat.logcat

/**
 * The [Application] instance for the app.
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidLogcatLogger.installOnDebuggableApp(this, minPriority = LogPriority.VERBOSE)
        logcat { "Application created" }
    }
}
