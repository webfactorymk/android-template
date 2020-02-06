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

package mk.webfactory.template.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import mk.webfactory.template.di.scope.ActivityScope
import mk.webfactory.template.feature.home.ui.HomeActivity
import mk.webfactory.template.feature.home.ui.HomePresenterModule

/**
 * Binding of [Activities][Activity] within the [UserScope]
 * i.e. screens requiring logged in user to display user specific data.
 *
 * @see GlobalBindingModule AppBindingModule for global scope
 */
@Module
abstract class UserBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [HomePresenterModule::class])
    abstract fun homeActivity(): HomeActivity?
}