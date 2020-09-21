class LRUCache<K, V>(val cacheCapacity: Int) {
    private val map = mutableMapOf<K, DoubleLinkedList<K, V>.Node>()
    private val list = DoubleLinkedList<K, V>()

    fun put(key: K, value: V) {
        assert(map.size <= cacheCapacity)
        assert(map.size == list.size)
        val oldSize = map.size
        val exists = map.containsKey(key)

        doPut(key, value)

        assert(map.size <= cacheCapacity)
        assert(map.size == list.size)
        assert(exists && oldSize == map.size || !exists && (oldSize + 1 == map.size || oldSize == cacheCapacity))
    }

    fun get(key: K): V? {
        assert(map.size <= cacheCapacity)
        assert(map.size == list.size)
        val oldSize = map.size

        val result = doGet(key)

        assert(map.size <= cacheCapacity)
        assert(map.size == list.size)
        assert(map.size == oldSize)

        return result
    }

    private fun doPut(key: K, value: V) {
        map[key]?.run { remove() }

        list.addFirst(key, value)
        map[key] = list.getFirst()

        checkCapacity()
    }

    private fun checkCapacity() {
        assert(map.size == list.size)

        if (map.size <= cacheCapacity) return

        val excessEntry = list.getLast()
        list.removeLast()
        map.remove(excessEntry.key)

        assert(map.size == list.size)
        assert(map.size <= cacheCapacity)
    }

    private fun doGet(key: K): V? {
        assert(map.size == list.size)
        assert(map.size <= cacheCapacity)
        val oldSize = map.size

        val node = map[key] ?: return null
        doPut(key, node.value!!)

        assert(map.size == list.size)
        assert(map.size <= cacheCapacity)
        assert(map.size == oldSize)

        return node.value
    }
}
