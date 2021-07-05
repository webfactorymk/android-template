package mk.webfactory.androidtemplate.network.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import junit.framework.Assert.assertNull
import mk.webfactory.template.network.gson.ZonedDateTimeTypeAdapter
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.ZoneOffset
import java.time.ZonedDateTime

class ZonedDateTimeTypeAdapterTest {
    private val zonedDateTime = ZonedDateTime.of(1991, 9, 5, 15, 45, 0, 0, ZoneOffset.ofHours(1))
    private val zonedDateTimeJson = "\"1991-09-05T15:45:00.000+01:00\""

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(ZonedDateTime::class.java, ZonedDateTimeTypeAdapter())
        .create()

    @Test
    fun testSerializationItemEquals() {
        val serializedItem: String = gson.toJson(zonedDateTime)
        assertEquals("\"1991-09-05T15:45:00+01:00\"", serializedItem)
    }

    @Test
    fun testDeserialization() {
        val actualItem: ZonedDateTime = gson.fromJson(zonedDateTimeJson, ZonedDateTime::class.java)
        assertEquals(actualItem, zonedDateTime)
    }

    @Test
    fun serializeNull() {
        val actualJson: String = gson.toJson(null as ZonedDateTime?)
        assertEquals("null", actualJson)
    }

    @Test
    fun deserializeNull1() {
        val actualValue: ZonedDateTime? = gson.fromJson(null as String?, ZonedDateTime::class.java)
        assertNull(actualValue)
    }

    @Test
    fun deserializeNull2() {
        val actualValue: ZonedDateTime? = gson.fromJson("null", ZonedDateTime::class.java)
        assertNull(actualValue)
    }
}