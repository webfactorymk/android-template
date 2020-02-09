package mk.webfactory.storage

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

/**
 * Base tests for [Storage]
 */
abstract class BaseStorageTest {

    protected lateinit var storage: Storage<String>

    protected abstract fun createInstance(): Storage<String>

    val itemA = "A"

    @Before
    fun setUp() {
        storage = createInstance()
    }

    @Test
    fun testSaveOneFindOne() {
        val saveOpResult: String = storage.save(itemA).blockingGet()
        val retrievedItem: String = storage.get().blockingGet()

        assertEquals(itemA, saveOpResult)
        assertEquals(itemA, retrievedItem)
    }

    @Test
    fun testSaveOneFindOneMultiline() {
        val itemMultiline = "line_1\nline_2\nline_3"

        val saveOpResult: String = storage.save(itemMultiline).blockingGet()
        val retrievedItem: String = storage.get().blockingGet()

        assertEquals(itemMultiline, saveOpResult)
        assertEquals(itemMultiline, retrievedItem)
    }

    @Test
    fun testSaveManyOverwrite() {
        val itemB = "B"
        val saveOpResultA = storage.save(itemA).blockingGet()
        val saveOpResultB = storage.save(itemB).blockingGet()
        val retrievedItem = storage.get().blockingGet()

        assertEquals(itemA, saveOpResultA)
        assertEquals(itemB, saveOpResultB)
        assertEquals(itemB, retrievedItem)
    }

    @Test
    fun testRetrieveNonExisting() {
        val retrievedItem: String? = storage.get().blockingGet()

        assertNull(retrievedItem)
    }

    @Test
    fun testDelete() {
        storage.save(itemA).blockingGet()

        storage.delete().blockingAwait()

        val retrievedItem: String? = storage.get().blockingGet()
        assertNull(retrievedItem)
    }

    @Test
    fun testDeleteNonExisting() {
        storage.delete().blockingAwait()

        val retrievedItem: String? = storage.get().blockingGet()
        assertNull(retrievedItem)
    }
}