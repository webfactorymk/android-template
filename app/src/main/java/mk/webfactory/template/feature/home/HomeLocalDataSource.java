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

import io.reactivex.Completable;
import io.reactivex.Maybe;
import mk.webfactory.template.data.storage.InMemoryStorage;

/**
 * Concrete implementation of a data source as in memory cache.
 */
public class HomeLocalDataSource implements HomeDataSource {

  private final String userId;
  private final InMemoryStorage<Object> storage;
  private boolean isCacheCorrupt;                 //implement any cache control here

  public HomeLocalDataSource(String userId, InMemoryStorage<Object> storage) {
    this.userId = userId;
    this.storage = storage;
    this.isCacheCorrupt = true;
  }

  @Override public Maybe<Object> getSomething() {
    if (!isCacheCorrupt) {
      return storage.get();
    } else {
      return Maybe.empty();
    }
  }

  @Override public void setSomething(Object object) {
//    storage.save(object).subscribeBy();
    isCacheCorrupt = false;
  }

  @Override public Completable deleteData() {
    isCacheCorrupt = true;
    return storage.delete();
  }

  @Override public boolean isDataAvailable() {
    return !isCacheCorrupt;
  }
}
