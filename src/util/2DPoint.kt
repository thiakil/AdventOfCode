package util

/**
 * Created by Thiakil on 6/12/2018.
 */
data class Point2D(val x: Int, val y: Int): Comparable<Point2D>{
    override fun compareTo(other: Point2D): Int = when (val res = Integer.compare(this.y, other.y)){
        0 -> Integer.compare(this.x, other.x)
        else -> res
    }

    operator fun plus(other: Point2D): Point2D = Point2D(this.x + other.x, this.y + other.y)

    fun manhattanDistanceFrom(other: Point2D): Int = Math.abs(this.x - other.x) + Math.abs(this.y - other.y)

    fun manhattanDistanceFrom(otherX: Int, otherY:Int): Int = Math.abs(this.x - otherX) + Math.abs(this.y - otherY)

    operator fun rangeTo(other: Point2D): RangePoint2D = RangePoint2D(this, other)

    companion object {
        val ZERO = Point2D(0, 0)
    }
}

class RangePoint2D(override val start: Point2D, override val endInclusive: Point2D) : ClosedRange<Point2D>, Iterable<Point2D>{
    override fun iterator(): Iterator<Point2D> = IteratorPoint2DSquare(start, endInclusive)
}

class IteratorPoint2DSquare(val first: Point2D, val last: Point2D): Iterator<Point2D>{
    private val step = if (last < first) -1 else 1
    private val finalElement = last
    private var hasNext: Boolean = if (step > 0) first <= last else first >= last
    private var next = if (hasNext) first else finalElement

    override fun hasNext(): Boolean = hasNext

    /**
     * Returns the next element in the iteration.
     */
    override fun next(): Point2D {
        val value = next
        if (value == finalElement) {
            if (!hasNext) throw kotlin.NoSuchElementException()
            hasNext = false
        }
        else {
            next = when (step){
                1 -> if (value.x == last.x){
                    Point2D(first.x, value.y + 1)
                } else {
                    Point2D(value.x + 1, value.y)
                }
                -1 -> if (value.x == first.x){
                    Point2D(last.x, value.y - 1)
                } else {
                    Point2D(value.x - 1, value.y)
                }
                else -> throw IllegalStateException("Unpossible")
            }
        }
        return value
    }
}

fun main(){
    for (point in Point2D(7, 0)..Point2D(10, 10)){
        println(point)
    }
}