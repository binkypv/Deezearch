package com.binkypv.deezearch

import android.app.Application
import com.binkypv.di.Modules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class DeezearchApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@DeezearchApp)
            modules(
                Modules.viewModelModule
            )
        }
    }
}