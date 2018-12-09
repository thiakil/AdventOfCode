package y2017

import util.*
import java.lang.UnsupportedOperationException
import java.util.function.Consumer

val directions = arrayOf(
    Point2D(1, 0),
    Point2D(0, -1),
    Point2D(-1, 0),
    Point2D(0, 1)
)

private class Y2017Day03 : util.Day(2017){

    override fun part1(): Any {
        val spiral = Spiral()
        for (it in 1 until 265149) {
            spiral.next()
        }
        return spiral.next().manhattanDistanceFrom(Point2D.ZERO)
    }

    override fun part2(): Any {
        val assignments = mutableMapOf<Point2D,Int>()
        val spiral = Spiral()
        assignments[spiral.next()] = 1
        while (assignments.values.max()!! <= 265149){
            val next = spiral.next()
            assignments[next] = assignments.entries.filter { it.key in next.adjacents() }.sumBy { it.value }
        }
        return (assignments.values.max()!!)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Y2017Day03().doParts()
        }
    }
}

private fun Point2D.adjacents():Array<Point2D> = arrayOf(
    this + Point2D(1,0),
    this + Point2D(1,-1),
    this + Point2D(0,-1),
    this + Point2D(-1,-1),
    this + Point2D(-1,0),
    this + Point2D(-1,1),
    this + Point2D(0,1),
    this + Point2D(1,1)
)

// First return is step #2 (1 being 0,0)
private class Spiral: Iterator<Point2D> {
    var pos = Point2D.ZERO
    var maxDist: Int = 1
    val directionIt = generateSequence(directions[0]) { directions[(directions.indexOf(it)+1) % directions.size] }.iterator()
    var direction = directionIt.next()
    var directionChanges: Int = 0

    override fun forEachRemaining(action: Consumer<in Point2D>) {
        throw UnsupportedOperationException("forEachRemaining: infinite")
    }

    override fun hasNext(): Boolean = true

    override fun next(): Point2D {
        val ret = pos
        pos += direction
        if ((direction.x == 0 && Math.abs(pos.y) == maxDist) || (direction.y == 0 && Math.abs(pos.x) == maxDist)){
            directionChanges = (directionChanges + 1) % 4
            direction = directionIt.next()
            if (directionChanges == 0){
                maxDist++
            }
        }
        return ret
    }
}