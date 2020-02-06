/*
 * MIT License
 *
 * Copyright (c) 2020 Web Factory LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package mk.webfactory.template.util

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.os.Looper

/**
 * Notifies implementations whenever the application has been in the background/foreground,
 * after state changes, for more than a {@value #NOTIFY_DELAY_TIME} milliseconds.
 *
 *
 * Use: (in Application class) [Application.registerActivityLifecycleCallbacks]
 *
 * @see .onApplicationEnteredForeground
 * @see .onApplicationEnteredBackground
 */
open class ActivityLifeCallbacks : ActivityLifeCallbacksAdapter {
    private val mHandler: Handler
    private var mStartedActivitiesCount = 0
    private val mNotifyActivityStartRunnable = Runnable { onApplicationEnteredForeground() }
    private val mNotifyActivityStopRunnable = Runnable { onApplicationEnteredBackground() }

    /** Initializes with a Handler posting on the main UI thread.  */
    constructor() {
        mHandler = Handler(Looper.getMainLooper())
    }

    constructor(handler: Handler) {
        mHandler = handler
    }

    protected open fun onApplicationEnteredForeground() {}
    protected open fun onApplicationEnteredBackground() {}

    override fun onActivityStarted(activity: Activity) {
        if (++mStartedActivitiesCount == 1) {
            val delayForNotify = delayForNotify
            if (delayForNotify != 0) {
                mHandler.removeCallbacks(mNotifyActivityStopRunnable)
                mHandler.postDelayed(mNotifyActivityStartRunnable, delayForNotify.toLong())
            } else {
                onApplicationEnteredForeground()
            }
        }
    }

    override fun onActivityStopped(activity: Activity) {
        if (--mStartedActivitiesCount == 0) {
            val delayForNotify = delayForNotify
            if (delayForNotify != 0) {
                mHandler.removeCallbacks(mNotifyActivityStartRunnable)
                mHandler.postDelayed(mNotifyActivityStopRunnable, delayForNotify.toLong())
            } else {
                onApplicationEnteredBackground()
            }
        }
    }

    companion object {
        /**
         * @return time to wait for another Activity from the application to be started before reporting
         * foreground/background events. `0` reports the event immediately.
         * @see .NOTIFY_DELAY_TIME
         */
        protected const val delayForNotify = 1500
    }
}

abstract class ActivityLifeCallbacksAdapter : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle) {}

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}
}