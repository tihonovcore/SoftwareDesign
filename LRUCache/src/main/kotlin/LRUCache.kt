import java.util.*

class LRUCache<K, V>(val cacheCapacity: Int) {
//    inner class Node<V>(val value: V, val iterator: MutableIterator<Entry<V>>)
    inner class Entry<V>(val key: K, val value: V)

    private val map = mutableMapOf<K, DoubleLinkedList<Entry<V>>.Node>()
//    private val list = LinkedList<Entry<V>>()
    private val list = DoubleLinkedList<Entry<V>>()

    fun put(key: K, value: V) {
        assert(map.size <= cacheCapacity)
        assert(map.size == list.size)
        val oldSize = map.size
        val exists = map.containsKey(key)

        doPut(key, value)

        assert(map.size <= cacheCapacity)
        assert(map.size == list.size)
//        assert(exists && oldSize == map.size || !exists && oldSize + 1 == map.size) //todo: what if !exists && oldSize == cacheCapacity
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

        list.addFirst(Entry(key, value))
        map[key] = list.getFirst()

        checkCapacity()
    }

    private fun checkCapacity() {
        assert(map.size == list.size)

        if (map.size <= cacheCapacity) return

        val excessEntry = list.getLast()
        list.removeLast()
        map.remove(excessEntry.value!!.key)

        assert(map.size == list.size)
        assert(map.size <= cacheCapacity)
    }

    private fun doGet(key: K): V? {
        assert(map.size == list.size)
        assert(map.size <= cacheCapacity)
        val oldSize = map.size

        val node = map[key] ?: return null
        doPut(key, node.value!!.value)

        assert(map.size == list.size)
        assert(map.size <= cacheCapacity)
        assert(map.size == oldSize)

        return node.value!!.value
    }
}

class DoubleLinkedList<T> {
    private var head = Node(null, null, null)
    private var tail = head

    var size: Int = 0

    fun removeLast() {
        val newTail = tail.prev!!
        newTail.next = null
        tail = newTail

        size--
    }

    fun addFirst(value: T) {
        val newHead = Node(null, head, value)
        head.prev = newHead
        head = newHead

        size++
    }

    fun getFirst(): Node {
        return head
    }

    fun getLast(): Node {
        return tail.prev!!
    }

    inner class Node(var prev: Node?, var next: Node?, var value: T?) {
        fun remove() {
            prev?.next = next
            next?.prev = prev

            size--
        }
    }
}
