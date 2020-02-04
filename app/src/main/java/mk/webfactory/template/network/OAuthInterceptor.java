package mk.webfactory.template.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import java.io.IOException;
import mk.webfactory.template.data.rx.Observables;
import mk.webfactory.template.data.rx.StubDisposableObserver;
import mk.webfactory.template.model.auth.AccessToken;
import mk.webfactory.template.user.UserDataWrapper;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

final class OAuthInterceptor implements Interceptor {

  private AccessToken accessToken;
  private Disposable userUpdateStreamDisposable;

  OAuthInterceptor() {}

  OAuthInterceptor(AccessToken accessToken) {
    this.accessToken = accessToken;
  }

  void setUserUpdateStream(@NonNull Observable<UserDataWrapper> userUpdateStream) {
    Observables.dispose(userUpdateStreamDisposable);
    userUpdateStreamDisposable = userUpdateStream
        .map(new Function<UserDataWrapper, AccessToken>() {
          @Override public AccessToken apply(UserDataWrapper userDataWrapper) throws Exception {
            return userDataWrapper.getAccessToken();
          }
        })
        .subscribeWith(new StubDisposableObserver<AccessToken>() {
          @Override public void onNext(final AccessToken accessToken) {
            if (AccessToken.VOID.equals(accessToken)) {
              updateAccessToken(null);
            } else {
              updateAccessToken(accessToken);
            }
          }
        });
  }

  void disposeUserUpdateStream() {
    Observables.dispose(userUpdateStreamDisposable);
  }

  private synchronized void updateAccessToken(@Nullable AccessToken accessToken) {
    this.accessToken = accessToken;
  }

  private synchronized AccessToken getAccessToken() {
    return accessToken;
  }

  @Override public Response intercept(@NonNull Chain chain) throws IOException {
    final AccessToken accessToken = getAccessToken();
    if (accessToken != null) {
      Request request = chain.request().newBuilder()
          .header("Authorization", "Bearer " + accessToken.getToken())
          .build();
      return chain.proceed(request);
    } else {
      return chain.proceed(chain.request());
    }
  }
}

