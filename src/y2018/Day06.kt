package y2018

import util.Day
import util.Point2D
import kotlin.collections.ArrayList

/**
 * Created by Thiakil on 1/12/2018.
 */

class Day06 : Day(2018){
    val testData=("1, 1\n" +
            "1, 6\n" +
            "8, 3\n" +
            "3, 4\n" +
            "5, 5\n" +
            "8, 9").split("\n")
    val parsed = lines.map {
        Point2D(
            it.substring(0 until it.indexOf(',')).toInt(),
            it.substring(it.indexOf(',') + 2).toInt()
        )
    }
    val minX = parsed.minBy { it.x }!!.x
    val maxX = parsed.maxBy { it.x }!!.x
    val minY = parsed.minBy { it.y }!!.y
    val maxY = parsed.maxBy { it.y }!!.y
    val nonInfinite = Array(parsed.size) { i-> val it = parsed[i]; it.x > minX && it.y > minY && it.x < maxX && it.y < maxY}
    val region = ArrayList<Point2D>().also { it.addAll(Point2D(minX, minY)..Point2D(maxX, maxY))}

    init {
        println("X: ${maxX-minX}, Y: ${maxY-minY}, size ${parsed.size}")
    }

    override fun part1(): Any {
        //val pointToClosestCoord = mutableMapOf<Point,Point>()//test point -> nearest coord point IF unique
        //val coordToNumLocs = util.CountingMap<util.Point2D>(1)//number of times this coord came up as closest
        val tally = Array(parsed.size) {0}
        for (test in region){
            //val test = util.Point2D(x,y)
            var minDistance = -1
            var closestIdx = -1
            var failed = false
            parsed.forEachIndexed { i, it ->
                val d = it.manhattanDistanceFrom(test)
                if (minDistance == -1 || d < minDistance) {
                    minDistance = d
                    closestIdx = i
                    failed = false
                } else if (d==minDistance){
                    failed = true
                }
            }

            if (!failed && closestIdx != -1 && nonInfinite[closestIdx]){
                tally[closestIdx]++
            }
        }


        return tally.max()!!
    }

    override fun part2(): Any {
        val targetDist = 10000
        var targetArea = 0
        for (y in minY..maxY) {
            for (x in minX..maxX) {
                var matched = false
                if (parsed.sumBy { it.manhattanDistanceFrom(x,y) } < targetDist){
                    targetArea++
                    matched = true
                }
                when (parsed.indexOf(Point2D(x, y))){
                    -1 -> if (matched){
                        print("X")
                    } else
                        print(".")
                    else -> print('O')
                }
            }
            println()
        }
        return targetArea
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day06().doParts()
        }
    }
}

data class ManhattanRes(val p: Point2D, val man: Int): Comparable<ManhattanRes> {

    override fun compareTo(other: ManhattanRes): Int = Integer.compare(this.man, other.man)

}