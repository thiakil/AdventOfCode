package y2017

import java.util.*

private class Day09 : util.Day(2017){

    data class Group(val parent: Group?){
        val score:Int = (parent?.score ?: 0) + 1
    }

    val parser = { it:List<String>->
        it.first().toCharArray().toList()
    }

    val doTest = 0
    val testData = ("").split("\n")
    val parsedLines = parser(when(doTest){
        0 -> lines
        else -> testData
    })

    var garbageCount = 0

    val groups = LinkedList<Group>().also { groups->
        val opengroups = LinkedList<Group>()
        val queue = LinkedList(parsedLines)
        while (queue.peekFirst()!= null){
            when(queue.pop()){
                '{' -> Group(opengroups.peek()).also {
                    opengroups.push(it)
                    groups.add(it)
                }
                '<' -> {//process garbage
                    loop@ while(true){
                        when (queue.pop()){
                            '>' -> break@loop
                            '!' -> queue.pop()
                            else -> garbageCount++
                        }
                    }
                }
                '}' -> opengroups.pop()
            }
        }
    }

    override fun part1(): Any {
        return groups.sumBy { it.score }
    }

    override fun part2(): Any {
        return garbageCount
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day09().doParts()
        }
    }
}