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

package mk.webfactory.template.user

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import mk.webfactory.template.config.USER_DATA_FILE
import mk.webfactory.template.data.storage.FlatFileStorage
import mk.webfactory.template.data.storage.JsonConverter.GsonConverter
import mk.webfactory.template.data.storage.Storage
import mk.webfactory.template.di.qualifier.ApplicationContext
import mk.webfactory.template.model.user.UserSession
import java.io.File
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
class UserModule {
    @Qualifier
    @MustBeDocumented
    annotation class Internal

    @Provides
    @Internal
    fun provideUserUpdatesSubject(): BehaviorSubject<UserSession> {
        return BehaviorSubject.create<UserSession>()
    }

    @Provides
    @Named("user_updates")
    fun provideUserUpdates(@Internal updateStream: BehaviorSubject<UserSession>)
            : Observable<UserSession> {
        return UserManager.hideUpdatesStream(updateStream)
    }

    @Provides
    @Singleton
    fun provideFlatFileStorage(@ApplicationContext context: Context, gson: Gson)
            : Storage<UserSession> {
        return FlatFileStorage(
            UserSession::class.java,
            File(context.filesDir, USER_DATA_FILE),
            GsonConverter(gson)
        )
    }

    @Provides
    @Singleton
    fun provideUserManager(
        @Internal userUpdates: BehaviorSubject<UserSession>,
        userStorage: Storage<UserSession>
    ): UserManager<UserSession> {
        return UserManager(userUpdates, userStorage)
    }
}