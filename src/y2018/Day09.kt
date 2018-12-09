package y2018

import util.*

private class Day09 : Day(2018){

    val re = Regex("(?<players>\\d+) players; last marble is worth (?<last>\\d+) points")

    val parser = { it:List<String>->
        it.first().map(re) { it }
    }

    val doTest = 0
    val testData = ("13 players; last marble is worth 7999 points").split("\n")
    val parsedLines = parser(when(doTest){
        0 -> lines
        else -> testData
    })

    val players = parsedLines["players"].toInt()
    val lastMarble = parsedLines["last"].toLong()

    fun runGame(maxMarble: Long): Long {
        var scores = CircularListNode.sized(players){0L}
        var current = CircularListNode(0L)
        for (marble in 1..maxMarble){
            if (marble % 23 == 0L) {
                scores.data += marble
                current = current[-7]
                scores.data += current.data
                current = current.removeThis()
            } else {
                current++
                current += marble
            }
            scores++
        }
        return scores.max()!!
    }

    override fun part1(): Any = runGame(lastMarble)

    override fun part2(): Any = runGame(lastMarble*100L)

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day09().doParts()
        }
    }
}