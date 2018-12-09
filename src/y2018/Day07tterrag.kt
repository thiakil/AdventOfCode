package y2018

import util.Day
import java.util.PriorityQueue

class Day07tterrag : Day(2018) { override val filename: String get() = "Day07"

    private val roots = mutableMapOf<String, Node>().also { nodes ->
        lines.map { PATTERN.matchEntire(it)!!.groupValues }.forEach {
            val prev = nodes.computeIfAbsent(it[1]) { Node(it) }
            val next = nodes.computeIfAbsent(it[2]) { Node(it) }
            prev.next.add(next)
            next.prev.add(prev)
        }
    }.values.filter { n -> n.prev.isEmpty() }

    data class Node(val id: String) : Comparable<Node> {
        var next = mutableListOf<Node>()
        var prev = mutableListOf<Node>()

        override fun compareTo(other: Node): Int = compareValuesBy(this, other) { it.id }
    }

    override fun part1(): Any {
        val toSearch = PriorityQueue(roots)
        val path = LinkedHashSet<Node>()
        while (!toSearch.isEmpty()) {
            toSearch.poll().run {
                path.add(this)
                toSearch.addAll(next.filter { path.containsAll(it.prev) })
            }
        }
        return path.joinToString("") { it.id }
    }

    override fun part2(): Any {
        val inProgress = mutableMapOf<Node, Int>()
        val toSearch = PriorityQueue(roots)
        val path = mutableSetOf<Node>()
        var timePassed = 0
        while (true) {

            if (!toSearch.isEmpty()) {
                while (inProgress.size < 5 && !toSearch.isEmpty()) {
                    val next = toSearch.poll()
                    inProgress[next] = next.id[0].toInt() - 4
                }
            } else if (inProgress.isEmpty()) {
                break
            }

            val nextUp = inProgress.entries.minBy { it.value }!!
            inProgress.remove(nextUp.key)
            inProgress.replaceAll { _, e -> e - nextUp.value }

            path += nextUp.key
            toSearch.addAll(nextUp.key.next.filter { no -> path.containsAll(no.prev) })

            timePassed += nextUp.value
        }
        return timePassed
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day07tterrag().doParts()
        }

        private val PATTERN = Regex("Step (\\w+) must be finished before step (\\w+) can begin.")
    }
}