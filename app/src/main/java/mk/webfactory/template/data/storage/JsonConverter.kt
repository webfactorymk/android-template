package mk.webfactory.template.data.storage

import com.google.gson.Gson
import java.lang.reflect.Type

interface JsonConverter {
    @Throws(Exception::class)
    fun toJson(o: Any): String

    @Throws(Exception::class)
    fun <T> fromJson(json: String, type: Type): T

    class GsonConverter(private val gson: Gson) : JsonConverter {
        override fun toJson(o: Any): String {
            return gson.toJson(o)
        }

        override fun <T> fromJson(json: String, type: Type): T {
            return gson.fromJson(json, type)
        }

    }
}
