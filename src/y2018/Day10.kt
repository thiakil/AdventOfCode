package y2018

import util.*
import java.util.*

private class Day10 : Day(2018){

    val re = Regex("position=<\\s*(?<x>-?\\d+),\\s*(?<y>-?\\d+)> velocity=<\\s*(?<dx>-?\\d+),\\s*(?<dy>-?\\d+)>")

    data class Point(var x:Int, var y: Int, val dX: Int, val dY: Int): Comparable<Point>{
        fun apply(step: Int){
            x += (dX*step)
            y+= (dY*step)
        }

        override fun compareTo(other: Point): Int = cmp.compare(this, other)

        companion object {
            val cmp = compareBy<Point> { it.y }.thenComparingInt { it.x }
        }
    }

    val parser = { it:List<String>->
        it.map(re) { Point(it["x"].toInt(), it["y"].toInt(), it["dx"].toInt(), it["dy"].toInt()) }.sorted()
    }

    val doTest = 0
    val testData = ("").split("\n")
    val parsedLines = parser(when(doTest){
        0 -> lines
        else -> testData
    })

    fun diff(points: List<Point>, key: (point: Point)->Int):Int{
        return key(points.maxBy(key)!!) - key(points.minBy(key)!!)
    }

    var iters = 0

    override fun part1(): Any {
        val points = parsedLines
        var lastX = diff(points) {it.x}
        var lastY = diff(points) { it.y }
        loop@ while (true){
            points.forEach { it.apply(1) }

            var x = diff(points) {it.x}
            var y = diff(points) { it.y }

            if (x>lastX || y> lastY){
                break
            }
            iters++
            lastX = x
            lastY = y
        }

        points.forEach { it.apply(-1) }
        //points.sort()
        for (y in points.minBy { it.y }!!.y..points.maxBy { it.y }!!.y){
            for (x in points.minBy { it.x }!!.x..points.maxBy { it.x }!!.x){
                print(when(points.find { it.x == x && it.y == y }){
                    null-> '.'
                    else-> '#'
                })
            }
            println()
        }
        return ""
    }

    override fun part2(): Any {
        return iters
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day10().doParts()
        }
    }
}