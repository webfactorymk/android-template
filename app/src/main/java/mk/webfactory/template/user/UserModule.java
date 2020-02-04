package mk.webfactory.template.user;

import android.content.Context;
import com.google.gson.Gson;
import dagger.Module;
import dagger.Provides;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import java.io.File;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Named;
import javax.inject.Qualifier;
import javax.inject.Singleton;
import mk.webfactory.template.config.GlobalConfig;
import mk.webfactory.template.data.storage.FlatFileStorage;
import mk.webfactory.template.data.storage.JsonConverter;
import mk.webfactory.template.data.storage.Storage;
import mk.webfactory.template.di.qualifier.ApplicationContext;

@Module public class UserModule {
  
  @Qualifier @Documented @Retention(RetentionPolicy.RUNTIME) @interface Internal {}

  private final BehaviorSubject<UserDataWrapper> userUpdateStream = BehaviorSubject.create();

  @Provides @Singleton
  public Storage<UserDataWrapper> provideFlatFileStorage(@ApplicationContext Context context) {
    File userFile = new File(context.getFilesDir(), GlobalConfig.USER_DATA_FILE);
    JsonConverter jsonConverter = new JsonConverter.GsonConverter(new Gson());
    return new FlatFileStorage<>(UserDataWrapper.class, userFile, jsonConverter);
  }

  @Provides @Named("user_update_stream")
  public Observable<UserDataWrapper> provideUserUpdateStream() {
    return userUpdateStream.hide();
  }

  @Provides @Internal
  BehaviorSubject<UserDataWrapper> provideUserUpdateStreamSource() {
    return userUpdateStream;
  }
}
