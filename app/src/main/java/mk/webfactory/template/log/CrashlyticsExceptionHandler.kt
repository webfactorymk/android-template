package mk.webfactory.template.log

import android.content.Context
import android.net.ConnectivityManager
import android.os.StatFs
import android.os.SystemClock
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.internal.common.CrashlyticsCore
import mk.webfactory.template.App
import timber.log.Timber
import java.util.concurrent.TimeUnit

internal class CrashlyticsExceptionHandler(
    private val crashlyticsCore: FirebaseCrashlytics,
    context: Context
) :
    Thread.UncaughtExceptionHandler {
    private val context: Context = context.applicationContext
    private val handler: Thread.UncaughtExceptionHandler =
        Thread.getDefaultUncaughtExceptionHandler()!!

    init {
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(
        thread: Thread,
        ex: Throwable
    ) {
        logCurrentDeviceState()
        logLastUsedPages()
        SystemClock.sleep(SLEEP_BEFORE_FORWARD)
        handler.uncaughtException(thread, ex)
    }

    private fun logLastUsedPages() {
        var output = "Last used pages:\n"
        val items = App.CRASH_REPORT.lastUsedPages()
        for (i in items.indices.reversed()) {
            output += items[i] + "\n"
        }
        Timber.e(output)
    }

    private fun logCurrentDeviceState() {
        val network =
            (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
        crashlyticsCore.setCustomKey(
            "networkAccess",
            if (network != null && network.isConnected) network.typeName else "NA"
        )
        crashlyticsCore.setCustomKey("heap", toMbString(Runtime.getRuntime().totalMemory()))
        crashlyticsCore.setCustomKey(
            "freeMemory",
            toMbString(Runtime.getRuntime().freeMemory())
        )
        crashlyticsCore.setCustomKey("maxHeap", toMbString(Runtime.getRuntime().maxMemory()))
        crashlyticsCore.setCustomKey("freeDisk", toMbString(freeDiskMemory))
    }

    private val freeDiskMemory: Long
        get() {
            val diskStats = StatFs(context.filesDir.path)
            return diskStats.availableBytes
        }

    private fun toMbString(bytes: Long): String {
        return String.format("%.2fMB", bytes.toFloat() / 1024 / 1024)
    }

    companion object {
        private val SLEEP_BEFORE_FORWARD =
            TimeUnit.SECONDS.toMillis(1)
    }

}