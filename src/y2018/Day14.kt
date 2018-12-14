package y2018

import util.*

class Day14 : Day(2018){

    val parser = { it:List<String>->
        it.first().toInt()
    }

    val doTest = 0
    val testData = ("").split("\n")
    val parsedLines = parser(when(doTest){
        0 -> lines
        else -> testData
    })

    val recipes = mutableListOf(3,7)
    val elfCurrent = IntArray(2) { it }

    override fun part1(): Any {
        while(findSubsequence(parsedLines.toString()) == -1){
            print("\r" + recipes.size)
            for (iter in 0..parsedLines*10) {
                recipes.addAll((recipes[elfCurrent[0]] + recipes[elfCurrent[1]]).toString().map {
                    it.toString().toInt()
                })
                for (elfI in elfCurrent.indices) {
                    elfCurrent[elfI] = (elfCurrent[elfI] + 1 + recipes[elfCurrent[elfI]]) % recipes.size
                }
            }
        }
        println()
        val buff = StringBuilder()
        for (i in parsedLines until parsedLines+10){
            buff.append(recipes[i])
        }
        return buff.toString()
    }

    fun findSubsequence(input: String): Int{
        val subseq = input.map { it.toString().toInt() }
        return recipes.windowed(subseq.size).lastIndexOf(subseq)
    }

    override fun part2(): Any {
        println("51589: 9 == "+findSubsequence("51589"))
        return findSubsequence(parsedLines.toString())
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day14().doParts()
        }
    }
}