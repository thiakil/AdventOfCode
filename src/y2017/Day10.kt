package y2017

import util.*

private class Day10 : util.Day(2017){

    val parser = { it:List<String>->
        it.first().split(",").ints()
    }

    val doTest = 0
    val testData = ("3,4,1,5").split("\n")
    val parsedLines = parser(when(doTest){
        0 -> lines
        else -> testData
    })

    override fun part1(): Any {
        val list1 = (0..255).toMutableList()
        var curPos = 0
        for ((skipSize, len) in parsedLines.withIndex()){
            /*val srcPositions = IntArray(len) { (curPos + it) % list1.size }
            val els = srcPositions.map { list1[it] }.asReversed()
            for (i in srcPositions.indices){
                list1[srcPositions[i]] = els[i]
            }*/
            LoopingListSubView(list1, curPos, len).reverse()
            curPos = (curPos + len + skipSize) % list1.size
        }
        return list1[0] * list1[1]
    }

    override fun part2(): Any {
        val lengths2 = lines.first().map { it.toInt() }.plus(listOf(17, 31, 73, 47, 23))
        var list2 = (0..255).toMutableList()
        var curPos = 0
        var skipSize = 0
        for (runNo in 1..64){
            for (len in lengths2){
                /*val srcPositions = IntArray(len) { (curPos + it) % list2.size }
                val els = srcPositions.map { list2[it] }.asReversed()
                for (i in srcPositions.indices){
                    list2[srcPositions[i]] = els[i]
                }*/
                LoopingListSubView(list2, curPos, len).reverse()
                curPos = (curPos + len + skipSize) % list2.size
                skipSize++
            }
        }
        return list2.windowed(16, 16).map { it.reduce { acc, i -> acc xor i} }.map {
            Integer.toHexString(it).padStart(2,'0')
        }.joinToString("")
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day10().doParts()
        }
    }
}