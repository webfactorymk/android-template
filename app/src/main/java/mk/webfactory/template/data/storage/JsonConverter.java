package mk.webfactory.template.data.storage;

import com.google.gson.Gson;
import java.lang.reflect.Type;

public interface JsonConverter {

  String toJson(Object o) throws Exception;

  <T> T fromJson(String json, Type type) throws Exception;

  class GsonConverter implements JsonConverter {

    private final Gson gson;

    public GsonConverter(Gson gson) {
      this.gson = gson;
    }

    @Override public String toJson(Object o) {
      return gson.toJson(o);
    }

    @Override public <T> T fromJson(String json, Type type) {
      return gson.fromJson(json, type);
    }
  }
}
