package util

fun <T> Sequence<T>.repeat() = sequence { while (true) yieldAll(this@repeat) }

fun IntProgression.repeatingIterator(): IntIterator = InfiniteIntProgressionIterator(this.first, this.last, this.step)

class InfiniteIntProgressionIterator(private val first: Int, last: Int, val step: Int) : IntIterator() {
    private val finalElement = last
    private var hasNext: Boolean = if (step > 0) first <= last else first >= last
    private var next = if (hasNext) first else finalElement
    init {
        if (first == last)
            throw IllegalArgumentException("start and end cannot be the same")
    }

    override fun hasNext(): Boolean = hasNext

    override fun nextInt(): Int {
        val value = next
        if (value == finalElement) {
            next = first
        }
        else {
            next += step
        }
        return value
    }
}