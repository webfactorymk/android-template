package mk.webfactory.template.network.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

/**
 * Note that the time zone is serialized/deserialized as is. To get local time use
 * {@link ZonedDateTime#withZoneSameInstant(ZoneId)} with {@link ZoneId#systemDefault()} for zone id.
 */
public class ZonedDateTimeTypeAdapter implements JsonSerializer<ZonedDateTime>, JsonDeserializer<ZonedDateTime> {

  @Override public ZonedDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    return ZonedDateTime.parse(json.getAsString());
  }

  @Override public JsonElement serialize(ZonedDateTime src, Type typeOfSrc, JsonSerializationContext context) {
    return new JsonPrimitive(src.format(DateTimeFormatter.ISO_DATE_TIME));
  }
}
