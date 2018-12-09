package y2018

import util.*

private class Day00 : Day(2018){

    val parser = { it:List<String>->
        it
    }

    val doTest = 0
    val testData = ("").split("\n")
    val parsedLines = parser(when(doTest){
        0 -> lines
        else -> testData
    })

    override fun part1(): Any {
        return "incomplete"
    }

    override fun part2(): Any {
        return "incomplete"
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day00().doParts()
        }
    }
}