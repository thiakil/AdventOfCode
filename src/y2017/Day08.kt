package y2017

import util.*

private class Day08 : util.Day(2017){

    val registers1 = ComputedMap<String,Int>{0}
    var maxVal = 0

    data class Operation(val register: String, val dir: String, val value: Int, val testReg: String, val logical: (Int,Int)->Boolean, val testVal: Int)

    val parser = { it:List<String>->
        it.map(Regex("(?<register>\\w+) (?<dir>dec|inc) (?<val>-?\\d+) if (?<cond>\\w+) (?<op>[=<>!]+) (?<test>-?\\d+)")) {
            Operation(it["register"], it["dir"], it["val"].toInt(), it["cond"], BooleanOp.from(it["op"]), it["test"].toInt())
        }
    }

    val doTest = 0
    val testData = ("").split("\n")
    val parsedLines = parser(when(doTest){
        0 -> lines
        else -> testData
    })

    override fun part1(): Any {
        for (op in parsedLines){
            if (op.logical(registers1[op.testReg], op.testVal)){
                val cur = registers1[op.register]
                registers1[op.register] = when (op.dir){
                    "inc" -> cur + op.value
                    "dec" -> cur - op.value
                    else -> throw IllegalStateException(op.dir)
                }
                if (registers1[op.register] > maxVal)
                    maxVal = registers1[op.register]
            }
        }
        return registers1.values.max()!!
    }

    override fun part2(): Any = maxVal

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day08().doParts()
        }
    }
}