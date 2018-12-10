package y2017

private class Day13 : util.Day(2017){

    data class Scanner(val depth: Int, val range: Int){
        fun posAtStart(picosecond:Int):Int {
            val pos = picosecond % ((range*2)-2)
            return when {
                pos < range -> pos
                else -> range - (pos-range)-2
            }
        }

        val severity = depth * range
    }

    val parser = { it:List<String>->
        it.map { Scanner(it.substring(0, it.indexOf(':')).toInt(),it.substring(it.indexOf(':')+2).toInt())}
    }

    val doTest = 0
    val testData = ("0: 3\n" +
            "1: 2\n" +
            "4: 4\n" +
            "6: 4").split("\n")
    val parsedLines = parser(when(doTest){
        0 -> lines
        else -> testData
    })

    val maxDepth = parsedLines.maxBy { it.depth }!!.depth

    data class TraverseResult(val severity: Int, val caught: Boolean)

    fun traverse(startSecond: Int): TraverseResult{
        var severity = 0
        var caught = false
        for (sec in 0..maxDepth){
            when (val scanner = parsedLines.find { it.depth == sec }){
                is Scanner -> if (scanner.posAtStart(sec+startSecond) == 0) {
                    //println("Caught at layer $sec")
                    severity += scanner.severity
                    caught = true
                }
            }
        }
        return TraverseResult(severity, caught)
    }

    override fun part1(): Any {
        return traverse(0)
    }

    override fun part2(): Any = (0..20000000).find { !traverse(it).caught }!!

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day13().doParts()
        }
    }
}