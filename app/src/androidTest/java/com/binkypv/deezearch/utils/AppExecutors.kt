package com.binkypv.deezearch.utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor

open class AppExecutors(
    val diskIO: Executor,
    val networkIO: Executor,
    val mainThread: Executor,
) {

    fun diskIO(): Executor {
        return diskIO
    }

    fun networkIO(): Executor {
        return networkIO
    }

    fun mainThread(): Executor {
        return mainThread
    }

    class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}