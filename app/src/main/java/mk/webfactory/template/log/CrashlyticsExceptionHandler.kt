package mk.webfactory.template.log

import android.annotation.TargetApi
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.StatFs
import android.os.SystemClock
import com.crashlytics.android.core.CrashlyticsCore
import mk.webfactory.template.App
import timber.log.Timber
import java.util.concurrent.TimeUnit

internal class CrashlyticsExceptionHandler(
    private val crashlyticsCore: CrashlyticsCore,
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
        crashlyticsCore.setString(
            "networkAccess",
            if (network != null && network.isConnected) network.typeName else "NA"
        )
        crashlyticsCore.setString("heap", toMbString(Runtime.getRuntime().totalMemory()))
        crashlyticsCore.setString(
            "freeMemory",
            toMbString(Runtime.getRuntime().freeMemory())
        )
        crashlyticsCore.setString("maxHeap", toMbString(Runtime.getRuntime().maxMemory()))
        crashlyticsCore.setString("freeDisk", toMbString(freeDiskMemory))
    }

    @get:TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private val freeDiskMemory: Long
        get() {
            val diskStats = StatFs(context.filesDir.path)
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                diskStats.availableBytes
            } else (diskStats.availableBlocks * diskStats.blockSize).toLong()
        }

    private fun toMbString(bytes: Long): String {
        return String.format("%.2fMB", bytes.toFloat() / 1024 / 1024)
    }

    companion object {
        private val SLEEP_BEFORE_FORWARD =
            TimeUnit.SECONDS.toMillis(1)
    }

}