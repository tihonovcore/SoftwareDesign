import junit.framework.TestCase
import org.junit.Test

class TestLRUCache : TestCase() {
    @Test
    fun testPutGet() {
        val cache = LRUCache<Int, Int>(5)

        fun putGet(x: Int) {
            cache.put(x, x)
            assert(cache.get(x) == x)
        }

        listOf(3, 4, 5, 6, 7).forEach { putGet(it) }
    }

    @Test
    fun testGetGetNonExists() {
        val cache = LRUCache<Int, Int>(5)

        cache.put(1, 1)
        cache.put(2, 2)

        assert(cache.get(3) == null)
    }

    @Test
    fun testRemoving() {
        val cache = LRUCache<Int, Int>(5)

        val list = listOf(4, 5, 6, 7, 8, 9, 10)
        list.forEach { cache.put(it, it) }
        val data = list.map { cache.get(it) }

        assert(data == listOf(null, null, 6, 7, 8, 9, 10))
    }

    @Test
    fun testReorderingByGet() {
        val cache = LRUCache<Int, Int>(5)

        val list = listOf(2, 1, 0, -1, -2)
        list.forEach { cache.put(it, it) }
        cache.get(list[1])
        cache.get(list[0])
        listOf(3, 4, 5).forEach { cache.put(it, it) }
        val data = listOf(0, 1, 2, -1, -2, 3, 4, 5).map { cache.get(it) }

        assert(data == listOf(null, 1, 2, null, null, 3, 4, 5)) { println(data) }
    }

    @Test
    fun testReorderingByPut() {
        val cache = LRUCache<Int, Int>(5)

        val list = listOf(2, 1, 0, -1, -2)
        list.forEach { cache.put(it, it) }
        cache.put(list[1], list[1])
        cache.put(list[0], list[0])
        listOf(3, 4, 5).forEach { cache.put(it, it) }
        val data = listOf(0, 1, 2, -1, -2, 3, 4, 5).map { cache.get(it) }

        assert(data == listOf(null, 1, 2, null, null, 3, 4, 5)) { println(data) }
    }
}
