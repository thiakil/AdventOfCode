package y2017

import util.*

private class Day06 : util.Day(2017){

    val parser = { it:List<String>->
        it.first().split("\t").map { it.toInt() }//.toIntArray()
    }

    val doTest = 0
    val testData = ("0 2 7 0").replace(' ', '\t').split("\n")
    val parsedLines = parser(when(doTest){
        0 -> lines
        else -> testData
    })

    val recurse = {banks:MutableList<Int> ->
        var toDistribute = banks.max()!!
        var idx = banks.indexOf(toDistribute)
        banks[idx] = 0
        idx++
        while (toDistribute > 0){
            banks[(idx++)%banks.size]++
            toDistribute--
        }
    }

    override fun part1(): Any {
        return parsedLines.findFirstRepetitionState(recurse).steps
    }

    override fun part2(): Any {
        return parsedLines.findFirstRepetitionState(recurse).state.findFirstRepetitionState(recurse).steps
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day06().doParts()
        }
    }
}

