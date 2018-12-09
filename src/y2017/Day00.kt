package y2017
import util.*

private class Day00 : util.Day(2017){

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
        TODO("not implemented")
    }

    override fun part2(): Any {
        TODO("not implemented")
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day00().doParts()
        }
    }
}