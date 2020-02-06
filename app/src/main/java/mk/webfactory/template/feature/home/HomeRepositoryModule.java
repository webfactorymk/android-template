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

package mk.webfactory.template.feature.home;

import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import mk.webfactory.template.data.storage.InMemoryStorage;
import mk.webfactory.template.di.qualifier.Local;
import mk.webfactory.template.di.qualifier.Remote;
import mk.webfactory.template.network.api.UserService;
import mk.webfactory.template.model.user.User;

@Module
public class HomeRepositoryModule {

  @Provides
  @Named("userId")
  String provideUserId(User user) {
    return user.getId();
  }

  @Provides
  @Local
  HomeDataSource provideHomeLocalDataSource(@Named("userId") String userId) {
    return new HomeLocalDataSource(userId, new InMemoryStorage<>());
  }

  @Provides
  @Remote
  HomeDataSource provideHomeRemoteDataSource(@Named("userId") String userId, UserService userService) {
    return new HomeRemoteDataSource(userId, userService);
  }
}
