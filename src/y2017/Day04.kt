package y2017

private class Day04 : util.Day(2017){

    val parser = { it:List<String>->
        it.map { it.split(" ") }
    }

    val doTest = 0
    val testData = ("").split("\n")
    val parsedLines = parser(when(doTest){
        0 -> lines
        else -> testData
    })

    private val distincts = parsedLines.filter { it.distinct().size == it.size }

    override fun part1(): Any {
        return distincts.size
    }

    override fun part2(): Any {
        return distincts.map { it.map { it.toCharArray().sorted() } }.filter { it.distinct().size == it.size }.size
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day04().doParts()
        }
    }
}