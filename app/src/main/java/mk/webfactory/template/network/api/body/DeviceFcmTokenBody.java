package mk.webfactory.template.network.api.body;

public final class DeviceFcmTokenBody {

  //@SerializedName("device_token") todo
  private final String deviceToken;

  public DeviceFcmTokenBody(String deviceToken) {
    this.deviceToken = deviceToken;
  }
}
