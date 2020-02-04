package mk.webfactory.template.user;

import mk.webfactory.template.model.auth.AccessToken;

public final class UserDataWrapper {

  private final User user;
  private final AccessToken accessToken;

  private UserDataWrapper(User user, AccessToken accessToken) {
    this.user = user;
    this.accessToken = accessToken;
  }

  public static UserDataWrapper from(User user, AccessToken accessToken) {
    return new UserDataWrapper(user, accessToken);
  }

  public User getUser() {
    return user;
  }

  public AccessToken getAccessToken() {
    return accessToken;
  }

  @Override public String toString() {
    return getClass().getSimpleName() + "[" +
        "user=" + user + ", " +
        "accessToken=" + accessToken + "]";
  }
}