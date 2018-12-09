package y2018

import util.*
import java.util.*

private class Day08 : Day(2018){

    data class Node(val children: List<Node>, val metadata: List<Int>){
        val value: Int = when (children.size){
            0 -> metadata.sum()
            else -> metadata.map { children.getOrNull(it-1)?.value ?: 0 }.sum()
        }
    }

    val allNodes = mutableListOf<Node>()

    fun readNode(data: Queue<Int>): Node {
        val nChild = data.poll()
        val nMeta = data.poll()
        val n = Node(
            (0 until nChild).map { readNode(data) },
            data.pop(nMeta)
            //(0 until nMeta).map { data.poll() }
        )
        allNodes += n
        return n
    }

    val rootNode = readNode(lines.first().split(" ").ints())

    override fun part1(): Any {
        return allNodes.sumBy { it.metadata.sum() }
    }

    override fun part2(): Any {
        return rootNode.value
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day08().doParts()
        }
    }
}