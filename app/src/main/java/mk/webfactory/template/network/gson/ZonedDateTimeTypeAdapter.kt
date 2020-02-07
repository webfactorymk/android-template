package mk.webfactory.template.network.gson

import com.google.gson.*
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.lang.reflect.Type

/**
 * Note that the time zone is serialized/deserialized as is. To get local time use
 * [ZonedDateTime.withZoneSameInstant] with [ZoneId.systemDefault] for zone id.
 */
class ZonedDateTimeTypeAdapter :
    JsonSerializer<ZonedDateTime>,
    JsonDeserializer<ZonedDateTime> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): ZonedDateTime {
        return ZonedDateTime.parse(json.asString)
    }

    override fun serialize(
        src: ZonedDateTime,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return JsonPrimitive(src.format(DateTimeFormatter.ISO_DATE_TIME))
    }
}