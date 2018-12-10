package y2017
import util.*

private class Day14 : util.Day(2017){

    val parser = { it:List<String>->
        it.first()
    }

    val doTest = 0
    val testData = ("flqrgnkx").split("\n")
    val parsedLines = parser(when(doTest){
        0 -> lines
        else -> testData
    })

    fun knotHash(input: String):String{
        val lengths2 = input.map { it.toInt() }.plus(listOf(17, 31, 73, 47, 23))
        var list2 = (0..255).toMutableList()
        var curPos = 0
        var skipSize = 0
        for (runNo in 1..64){
            for (len in lengths2){
                LoopingListSubView(list2, curPos, len).reverse()
                curPos = (curPos + len + skipSize) % list2.size
                skipSize++
            }
        }
        return list2.windowed(16, 16).map { it.reduce { acc, i -> acc xor i} }.map {
            Integer.toHexString(it).padStart(2,'0')
        }.joinToString("")
    }

    val rows = (0..127).map { knotHash("$parsedLines-$it")
        .map { Integer.parseInt(it.toString(), 16) }
        .flatMap {it.toString(2).padStart(4, '0').toList()}.map { it =='1' }.toMutableList()
    }

    override fun part1(): Any {

        for (row in 0..7){
            for (col in 0..7){
                print(if (rows[row][col]) '#' else '.')
            }
            println()
        }
        return rows.sumBy { it.sumBy { if (it) 1 else 0 } }
    }

    fun recurseFlipOff(x: Int, y: Int){
        rows[y][x] = false
        if (y > 0 && rows[y-1][x]){
            recurseFlipOff(x, y-1)
        }
        if (x > 0 && rows[y][x-1]){
            recurseFlipOff(x-1, y)
        }
        if (y < 127 && rows[y+1][x]){
            recurseFlipOff(x, y+1)
        }
        if (x < 127 && rows[y][x+1]){
            recurseFlipOff(x+1, y)
        }
    }

    override fun part2(): Any {
        var regions = 0
        for (x in 0..127){
            for (y in 0..127){
                if (rows[y][x]){
                    regions++
                    recurseFlipOff(x, y)
                }
            }
        }
        return regions
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day14().doParts()
        }
    }
}