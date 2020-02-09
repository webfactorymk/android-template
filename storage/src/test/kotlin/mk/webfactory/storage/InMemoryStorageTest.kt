package mk.webfactory.storage

/**
 * Tests for [InMemoryStorage].
 */
class InMemoryStorageTest : BaseStorageTest() {

    override fun createInstance() = InMemoryStorage<String>()
}