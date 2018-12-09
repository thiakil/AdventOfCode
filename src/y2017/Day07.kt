package y2017

import util.*

private class Day07 : util.Day(2017){

    data class Node(val id:String, var weight:Int = -1, var prev: Node? = null){
        val children: MutableList<Node> = mutableListOf()
        fun sumWeight():Int = this.weight+childrenWeight().sum();
        fun childrenWeight(): List<Int> = children.map { it.sumWeight() }
    }

    val nodes = ComputedMap<String,Node> { Node(it) }

    val parser = { it:List<String>->
        it.map(Regex("(?<id>\\w+) \\((?<weight>\\d+)\\)(?: -> (?<children>(?:\\w+, )*(?:\\w+)))?")) {
            val n = nodes[it["id"]]
            n.weight = it["weight"].toInt()
            val childrenIds = it.getNullable("children")
            if (childrenIds != null){
                n.children.addAll(childrenIds.split(", ").map {
                    val c = nodes[it]
                    c.prev = n
                    c
                })
            }
            return@map n
        }
    }

    val doTest = 0
    val testData = ("").split("\n")
    val parsedLines = parser(when(doTest){
        0 -> lines
        else -> testData
    })

    override fun part1(): Any {
        return nodes.values.filter { it.prev == null }
    }

    override fun part2(): Any {
        val toFix = parsedLines.filter { it.children.size > 0 && it.childrenWeight().sum()/it.children.size != it.childrenWeight()[0] }
            .first().children.groupBy { it.sumWeight() }.entries.groupBy { it.value.size }
        val wrongWeights = toFix[1]!![0]
        val otherWeights = toFix[toFix.keys.find { it != wrongWeights.key }!!]!![0]
        val diff = otherWeights.key - wrongWeights.key
        return wrongWeights.value[0].weight+diff
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day07().doParts()
        }
    }
}