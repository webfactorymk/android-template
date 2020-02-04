package mk.webfactory.template.user;

import android.support.annotation.NonNull;
import com.google.gson.annotations.SerializedName;

import static mk.webfactory.template.util.Preconditions.checkNotNull;

public final class User {

  @SerializedName("id")
  private final String id;

  public User(String id) {
    this.id = checkNotNull(id);
  }

  @NonNull public String getId() {
    return id;
  }
}
