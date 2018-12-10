package y2017
import util.*

private class Day12 : util.Day(2017){

    data class Node(val id: String){
        val links = mutableListOf<Node>()
    }

    val nodes = ComputedMap(::Node)

    val parser = { it:List<String>->
        it.map {
            val parts = it.split(" <-> ")
            val ret = nodes[parts[0]]
            ret.links.addAll(parts[1].split(", ").map { nodes[it] })
            ret
        }
    }

    val doTest = 0
    val testData = ("").split("\n")
    val parsedLines = parser(when(doTest){
        0 -> lines
        else -> testData
    })

    fun traverse(node: Node, visited: MutableSet<Node>){
        visited.add(node)
        node.links.filterNot { visited.contains(it) }.forEach{traverse(it, visited)}
    }

    override fun part1(): Any {
        val node0 = nodes["0"]
        val visited = mutableSetOf<Node>()
        traverse(node0, visited)
        return visited.size
    }

    override fun part2(): Any {
        val nodesCopy = parsedLines.toMutableList()
        var groups = 0
        val visited = mutableSetOf<Node>()
        while (nodesCopy.size > 0){
            groups++
            traverse(nodesCopy[0], visited)
            nodesCopy.removeAll(visited)
        }
        return groups
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day12().doParts()
        }
    }
}