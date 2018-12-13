package y2017
import util.*

private class Day15 : util.Day(2017){

    val parser = { it:List<String>->
        it.joinToString ("\n").map(Regex("Generator A starts with (\\d+)\nGenerator B starts with (\\d+)")) {Pair(it[1].toLong(), it[2].toLong())}
    }

    val doTest = 0
    val testData = ("Generator A starts with 65\n" +
            "Generator B starts with 8921").split("\n")
    val parsedLines = parser(when(doTest){
        0 -> lines
        else -> testData
    })

    override fun part1(): Any {
        var A = parsedLines.first
        var B = parsedLines.second
        var judge = 0
        for (i in 0 until 40000000){
            A = (A * 16807) % 2147483647
            B = (B * 48271) % 2147483647
            //println("$A\n$B\n")
            if ((A and 0xFFFF) == (B and 0xFFFF)){
                judge++
            }
        }
        return judge
    }

    override fun part2(): Any {
        var A = parsedLines.first
        var B = parsedLines.second
        var judge = 0
        for (i in 0 until 5000000){
            do {
                A = (A * 16807) % 2147483647
            } while (A % 4 != 0L)
            do {
                B = (B * 48271) % 2147483647
            }while (B % 8 != 0L)
            //println("$A\n$B\n")
            if ((A and 0xFFFF) == (B and 0xFFFF)){
                judge++
            }
        }
        return judge
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day15().doParts()
        }
    }
}