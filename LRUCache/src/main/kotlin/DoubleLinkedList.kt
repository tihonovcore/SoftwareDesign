import java.lang.IllegalStateException

class DoubleLinkedList<K, V> {
    var size = 0

    private val head = Node(null, null)
    private val tail = Node(null, null)

    init {
        head.next = tail
        tail.prev = head
    }

    fun getFirst(): Node {
        headTailAssert()

        val result = head.next
        require(result != null) { "Internal error. At least Tail expected after head"}

        if (size == 0) {
            throw IllegalStateException("List is empty")
        }

        return result
    }

    fun getLast(): Node {
        headTailAssert()

        val result = tail.prev
        require(result != null) { "Internal error. At least head expected before tail" }

        if (size == 0) {
            throw IllegalStateException("List is empty")
        }

        return result
    }

    fun removeLast() {
        headTailAssert()

        val last = getLast()
        assert(last.prev != null && last.next != null)
        assert(last.value != null)

        last.remove()

        headTailAssert()
    }

    fun addFirst(key: K, value: V): Node {
        headTailAssert()

        return Node(key, value, head.next, head).also {
            head.next = it
            it.next!!.prev = it

            size++

            headTailAssert()
        }
    }

    private fun headTailAssert() {
        assert(head.prev == null)
        assert(head.next != null)

        assert(tail.prev != null)
        assert(tail.next == null)

        if (size == 0) {
            assert(head.next == tail)
            assert(tail.prev == head)
        }
    }

    inner class Node(
        val key: K?,
        val value: V?,
        var next: Node? = null,
        var prev: Node? = null
    ) {
        fun remove() {
            require(prev != null) { "Internal error. Attempt to remove head" }
            require(next != null) { "Internal error. Attempt to remove tail" }

            prev!!.next = next
            next!!.prev = prev

            size--
        }
    }
}
