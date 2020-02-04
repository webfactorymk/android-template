package mk.webfactory.template.model.auth;

import com.google.gson.annotations.SerializedName;

import static mk.webfactory.template.util.Preconditions.checkNotNull;

public final class AccessToken {

  @SerializedName("access_token")
  private final String token;
  @SerializedName("token_type")
  private final String tokenType;
  @SerializedName("created_at")
  private final long createdAt;
  @SerializedName("expires_in")
  private final long expiresIn;

  public static final AccessToken VOID = new AccessToken("-1", "nil", -1, 0);

  public AccessToken(String accessToken, String tokenType, long createdAt, long expiresIn) {
    this.token = checkNotNull(accessToken);
    this.tokenType = checkNotNull(tokenType);
    this.createdAt = createdAt;
    this.expiresIn = expiresIn;
  }

  //system time can be manipulated by changing device time, but then the request will fail so it's OK
  public boolean isTokenExpired() {
    return createdAt != 0 && System.currentTimeMillis() > createdAt + expiresIn;
  }

  public String getToken() {
    return token;
  }

  public String getTokenType() {
    return tokenType;
  }

  public long getCreatedAt() {
    return createdAt;
  }

  public long getExpiresIn() {
    return expiresIn;
  }

  @Override public boolean equals(Object o) {
    if (this == o) { return true; }
    if (o == null || getClass() != o.getClass()) { return false; }

    final AccessToken that = (AccessToken) o;

    if (createdAt != that.createdAt) { return false; }
    if (expiresIn != that.expiresIn) { return false; }
    if (!token.equals(that.token)) { return false; }
    return tokenType.equals(that.tokenType);
  }

  @Override public int hashCode() {
    int result = token.hashCode();
    result = 31 * result + tokenType.hashCode();
    result = 31 * result + (int) (createdAt ^ (createdAt >>> 32));
    result = 31 * result + (int) (expiresIn ^ (expiresIn >>> 32));
    return result;
  }

  @Override public String toString() {
    return getClass().getSimpleName() + "[" +
        "token=" + token + ", " +
        "tokenType=" + getTokenType() + ", " +
        "createdAt=" + createdAt + ", " +
        "expiresIn=" + expiresIn + ", " +
        "isExpired=" + isTokenExpired() + "]";
  }
}