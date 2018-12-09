package util

import java.lang.UnsupportedOperationException
import java.util.*
import kotlin.math.absoluteValue

class RepetitionResult<T>(stateIn: MutableList<T>, var steps:Int = 0, val seen:MutableSet<MutableList<T>> = mutableSetOf(stateIn.toMutableList())){
    val state = seen.first()
}

fun <T> List<T>.findFirstRepetitionState(processor: (it:MutableList<T>)->Unit):RepetitionResult<T>{
    val state = RepetitionResult(this.toMutableList())
    while (true){
        processor(state.state)
        state.steps++
        if (!state.seen.add(state.state.toMutableList())){
            break
        }
    }
    return state
}

fun <T> List<T>.linked(): LinkedList<T> = LinkedList(this)

fun <T,U> Iterable<T>.mapLinked(mapper: (T)->U): LinkedList<U> = this.mapTo(LinkedList(), mapper)

fun <T> Queue<T>.pop(n: Int): List<T> {
    require(n >= 0) { "Requested element count $n is less than zero." }
    if (n == 0) return emptyList()
    if (n >= size) return toList()
    if (n == 1) return listOf(first())
    val list = ArrayList<T>(n)
    for (i in 1..n) {
        list.add(this.poll())
    }
    return list
}

fun List<String>.ints(): LinkedList<Int> = this.mapLinked { it.toInt() }

class LoopingListSubView<T>(private val srcList: MutableList<T>, startOffset:Int, override val size: Int): AbstractMutableList<T>(){
    private val srcPositions = IntArray(size) { (startOffset + it) % srcList.size }

    override fun add(index: Int, element: T) {
        throw UnsupportedOperationException("add")
    }

    override fun removeAt(index: Int): T {
        throw UnsupportedOperationException("removeAt")
    }

    override fun set(index: Int, element: T): T {
        require(index in 0 until size)
        val previous = srcList[srcPositions[index]]
        srcList[srcPositions[index]] = element
        return previous
    }

    override fun get(index: Int): T {
        require(index in 0 until size)
        return srcList[srcPositions[index]]
    }
}

class CircularListNode<T>(var data: T): Iterable<T> {
    var next: CircularListNode<T> = this
    var prev: CircularListNode<T> = this

    fun advance(times: Int = 1): CircularListNode<T> {
        var curr = this
        for (i in 0 until times) {
            curr = curr.next
        }
        return curr
    }

    fun rewind(times: Int = 1): CircularListNode<T> {
        var curr = this
        for (i in 0 until times) {
            curr = curr.prev
        }
        return curr
    }

    operator fun get(relative: Int):CircularListNode<T> = when {
        relative == 0 -> this
        relative < 0 -> rewind(relative.absoluteValue)
        else -> advance(relative)
    }

    operator fun inc():CircularListNode<T> = this.advance(1)
    operator fun dec():CircularListNode<T> = this.rewind(1)

    fun insertAfter(newElementData: T): CircularListNode<T> {
        val newNode = CircularListNode(newElementData)
        newNode.prev = this
        newNode.next = this.next
        this.next = newNode
        newNode.next.prev = newNode
        return newNode
    }

    operator fun plus(newElementData: T): CircularListNode<T> = insertAfter(newElementData)

    //returns new current!
    fun removeThis(): CircularListNode<T> {
        val myprev = this.prev
        val mynext = this.next
        myprev.next = mynext
        mynext.prev = myprev
        this.prev = this
        this.next = this
        return mynext
    }

    override fun iterator(): Iterator<T> = CircleIterator(this)

    companion object {
        fun <T> from(vararg elements: T): CircularListNode<T>{
            return when (elements.size){
                0 -> throw IllegalArgumentException("Need at least one element!")
                1 -> CircularListNode(elements[0])
                else -> CircularListNode(elements[0]).also { first ->
                    var cur = first
                    for (i in 1 until elements.size){
                        cur = cur.insertAfter(elements[i])
                    }
                }
            }
        }

        fun <T> from(elements: List<T>): CircularListNode<T>{
            return when (elements.size){
                0 -> throw IllegalArgumentException("Need at least one element!")
                1 -> CircularListNode(elements[0])
                else -> CircularListNode(elements[0]).also { first ->
                    var cur = first
                    for (i in 1 until elements.size){
                        cur = cur.insertAfter(elements[i])
                    }
                }
            }
        }

        fun <T> sized(count: Int, valueSupplier: (i: Int)->T):CircularListNode<T>{
            require(count > 0)
            val first = CircularListNode(valueSupplier(0))
            var cur = first
            for (i in 1 until count){
                cur = cur.insertAfter(valueSupplier(i))
            }
            return first
        }
    }

    class CircleIterator<T>(private val start: CircularListNode<T>): Iterator<T>{
        private var curr = start

        override fun hasNext(): Boolean = curr.next !== start

        override fun next(): T = (curr++).data
    }
}