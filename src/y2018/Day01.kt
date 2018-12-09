package y2018

import util.Day

/**
 * Created by Thiakil on 1/12/2018.
 */

class Day01 : Day(2018){
    override fun part1(): String {
        val num = lines.stream().map { Integer.parseInt(it) }.reduce { a,b->
            return@reduce a+b
        }.get()
        return num.toString()
    }

    override fun part2(): String {
        var num = 0
        val visited = mutableSetOf<Int>()
        val lines: Array<Int> = lines.stream().map { Integer.parseInt(it) }.toArray { arrayOfNulls<Int>(it)}
        while (true){
            for (adjustment in lines) {
                val freq = num+adjustment
                if (visited.contains(freq)){
                    return@part2 freq.toString()
                }
                visited.add(freq)
                num = freq
                //println(freq)
            }
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day01().doParts()
        }
    }
}