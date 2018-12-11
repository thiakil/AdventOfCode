package y2018

import util.*
import kotlin.math.max

private class Day11 : Day(2018){

    val parser = { it:List<String>->
        it.first().toInt()
    }

    val doTest = 0
    val testData = ("18").split("\n")
    val parsedLines = parser(when(doTest){
        0 -> lines
        else -> testData
    })

    //array 0 based, actual coords 1 based
    val grid = Array(300){IntArray(300)}

    override fun part1(): Any {
        for (x in 1..300){
            for (y in 1..300){
                val rackId = x + 10
                var powerLevel = (rackId * y) + parsedLines
                powerLevel *= rackId
                powerLevel = powerLevel.toString().let { it[it.length-3].toString().toInt() } - 5
                grid[x-1][y-1] = powerLevel
            }
        }
        var maxPower = 0
        var maxCoord: Point2D? = null
        for (startX in 1..298){
            for (startY in 1..298){
                val squarePower = (0 until 3).sumBy { xOff-> (0 until 3).sumBy { yOff-> grid[startX+xOff-1][startY+yOff-1] } }
                if (squarePower > maxPower){
                    maxPower = squarePower
                    maxCoord = Point2D(startX, startY)
                }
            }
        }
        require(maxCoord != null)
        return "$maxPower @ ${maxCoord.x},${maxCoord.y}"
    }

    fun bestGridSizeStartingAt(startX: Int, startY: Int):Pair<Int,Int>{
        var gridSize = 0
        var maxPower = 0
        var squarePower = 0
        for (offset in 0 until 301- max(startX, startY)) {
            //column
            for (y in (startY..(startY+offset))){
                squarePower += grid[startX + offset - 1][y - 1]
            }
            for (x in (startX..(startX+offset-1))){//minus one as we've already done the last square with columns
                squarePower += grid[x - 1][startY + offset - 1]
            }
            if (squarePower > maxPower) {
                maxPower = squarePower
                gridSize = offset +1
            }
        }
        return Pair(gridSize,maxPower)
    }

    override fun part2(): Any {
        val coord = (1..300).flatMap { x-> (1..300).map { Point2D(x, it) } }.parallelStream().map { Pair(it, bestGridSizeStartingAt(it.x, it.y)) }.max(
            compareBy { it.second.second }).get()

        return "${coord.first.x},${coord.first.y},${coord.second.first}"
    }

    //decent version
    /*override fun part2(): Any {
        var maxPower = 0
        val maxCoord = IntArray(2)
        var gridSize = 0
        for (startX in 1..300) {
            for (startY in 1..300) {
                var squarePower = 0
                for (offset in 0 until 301- max(startX, startY)) {
                    //column
                    for (y in (startY..(startY+offset))){
                        squarePower += grid[startX + offset - 1][y - 1]
                    }
                    for (x in (startX..(startX+offset-1))){//minus one as we've already done the last square with columns
                        squarePower += grid[x - 1][startY + offset - 1]
                    }
                    if (squarePower > maxPower) {
                        maxPower = squarePower
                        maxCoord[0] = startX
                        maxCoord[1] = startX
                        gridSize = offset +1
                    }
                }
            }
        }

        return "$maxPower @ ${maxCoord[0]},${maxCoord[1]},$gridSize"
    }*/

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day11().doParts()
        }
    }
}