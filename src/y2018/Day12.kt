package y2018

import util.*

private class Day12 : Day(2018){

    class GenRule(val conditions: List<Boolean>, val result: Boolean){
        fun matches(idx: Int, pots: List<Boolean>): Boolean {
            val l1 = if (idx>0) pots[idx-1] else false
            val l2 = if (idx>1) pots[idx-2] else false
            val r1 = if (idx+1 < pots.size) pots[idx+1] else false
            val r2 = if (idx+2 < pots.size) pots[idx+2] else false
            return pots[idx] == conditions[2] && l1 == conditions[1] && l2 == conditions[0] &&
                    r1 == conditions[3] && r2 == conditions[4]
        }
    }
    class ParseResult(val pots: List<Boolean>, val rules: List<GenRule>)

    val parser = { fileLines:List<String>->
        val state = fileLines.first().substring(15).map { potMapper(it) }
        val rules = (2 until fileLines.size).map { fileLines[it] }.map {
            GenRule(it.substring(0, 5).map { potMapper(it) }, potMapper(it[9]) )
        }
        ParseResult(state, rules)
    }

    private fun potMapper(it: Char): Boolean {
        return when (it) {
            '.' -> false
            '#' -> true
            else -> throw IllegalStateException()
        }
    }

    val doTest = 0
    val testData = ("initial state: #..#.#..##......###...###\n" +
            "\n" +
            "...## => #\n" +
            "..#.. => #\n" +
            ".#... => #\n" +
            ".#.#. => #\n" +
            ".#.## => #\n" +
            ".##.. => #\n" +
            ".#### => #\n" +
            "#.#.# => #\n" +
            "#.### => #\n" +
            "##.#. => #\n" +
            "##.## => #\n" +
            "###.. => #\n" +
            "###.# => #\n" +
            "####. => #").split("\n")
    val parsedLines = parser(when(doTest){
        0 -> lines
        else -> testData
    })

    val rules = parsedLines.rules

    override fun part1(): Any {
        var currentGen = (1..3).map { false }.toMutableList()
        currentGen.addAll(parsedLines.pots)
        currentGen.addAll((1..parsedLines.pots.size).map { false })
        println(" 0: "+currentGen.map { if (it) '#' else '.' }.joinToString(""))
        for (i in 1..20){
            val newGen = BooleanArray(currentGen.size).toMutableList()
            for (pot in 0 until currentGen.size){
                newGen[pot] = rules.find { it.matches(pot, currentGen) }?.result ?: false
            }
            currentGen = newGen
            //println("Generation $i, ${currentGen.sumBy { if (it) 1 else 0 }}")
            println(i.toString().padStart(2, ' ')+": "+currentGen.map { if (it) '#' else '.' }.joinToString("")+"  "+tally(currentGen))

        }
        return tally(currentGen)
    }

    override fun part2(): Any {
        var currentGen = (1..3).map { false }.toMutableList()
        currentGen.addAll(parsedLines.pots)
        currentGen.addAll((1..parsedLines.pots.size*20).map { false })

        var result = currentGen
        val seen = mutableSetOf(currentGen.toList())
        var iter = 0L
        while (true){
            val copy = result.toList()
            for (pot in 0 until currentGen.size){
                result[pot] = rules.find { it.matches(pot, copy) }?.result ?: false
            }
            // result = srcList
            if (!seen.add(result.subList(result.indexOfFirst { it }, result.indexOfLast { it }+1).toList())){
                break
            }
            println(result.filter { it }.count().toString()+" "+result.indexOfFirst { it })
            iter++
        }

        println(iter.toString()+": "+result.map { if (it) '#' else '.' }.joinToString(""))
        println()


        return tally(result)
    }

    private fun tally(currentGen: MutableList<Boolean>): Int {
        var curPot = -3
        var tally = 0
        currentGen.forEach {
            if (it)
                tally += curPot
            curPot++
        }
        return tally
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day12().doParts()
        }
    }
}