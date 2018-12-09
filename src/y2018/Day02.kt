package y2018

import util.Day

/**
 * Created by Thiakil on 1/12/2018.
 */

class Day02 : Day(2018){
    override fun part1(): String {
        val ids=lines
        var twoLetters = 0
        var threeLetters = 0
        for (id in ids){
            val counts = mutableMapOf<Char,Int>()
            for (char in id){
                counts.compute(char) { _,oldValue -> when(oldValue) {
                    null -> 1
                    else -> oldValue + 1
                }}
            }
            if (counts.containsValue(2)){
                twoLetters++
            }
            if (counts.containsValue(3))
                threeLetters++
        }
        return (twoLetters * threeLetters) .toString()
    }

    override fun part2(): String {
        val ids=lines
        for (id1 in ids){
            for (id2 in ids){
                if (id1 == id2)
                    continue
                var differents = 0
                var difIndex = -1
                for (i in id1.indices){
                    if (id1[i] != id2[i]){
                        differents++
                        difIndex = i
                        if (differents > 1)
                            break
                    }
                }
                if (differents == 1){
                    return id1.substring(0, difIndex)+id1.substring(difIndex+1)
                }
            }
        }
        return "???"
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day02().doParts()
        }
    }
}