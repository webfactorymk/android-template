package mk.webfactory.storage

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import java.lang.reflect.Type


/**
 * Tests for [FlatFileStorage].
 */
class FlatFileStorageTest : BaseStorageTest() {

    @get:Rule
    var temporaryFolder = TemporaryFolder()

    override fun createInstance() = FlatFileStorage<String>(
        String::class.java,
        temporaryFolder.newFile("tmp_test_ffs"),
        object : JsonConverter {
            override fun toJson(o: Any) = o.toString()
            @Suppress("UNCHECKED_CAST")
            override fun <T> fromJson(json: String, type: Type): T = json as T
        }
    )
}