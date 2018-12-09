package y2017

private class Day05 : util.Day(2017){

    val parser = { it:List<String>->
        it.map { it.toInt() }.toIntArray()
    }

    val doTest = 0
    val testData = ("0\n" +
            "3\n" +
            "0\n" +
            "1\n" +
            "-3").split("\n")
    val parsedLines = parser(when(doTest){
        0 -> lines
        else -> testData
    })

    override fun part1(): Any {
        val stack = parsedLines.clone()
        var idx = 0
        var steps = 0
        while (idx >=0 && idx < stack.size){
            val oldidx = idx
            idx += stack[oldidx]
            stack[oldidx]++
            steps++
        }
        return steps
    }

    override fun part2(): Any {
        val stack = parsedLines.clone()
        var idx = 0
        var steps = 0
        while (idx >=0 && idx < stack.size){
            steps++
            val oldidx = idx
            idx += stack[oldidx]
            if(stack[oldidx] >= 3){
                stack[oldidx]--
            } else {
                stack[oldidx]++
            }
        }
        return steps
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day05().doParts()
        }
    }
}