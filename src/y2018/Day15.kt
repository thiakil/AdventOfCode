package y2018

import util.*
import java.util.*

class Day15 : Day(2018){

    enum class SquareType{
        Open,Wall
    }

    inner class Creature(val type: Char, var pos: Point2D, var hp: Int = 200){
        val targetType = when(type){
            'G' -> 'E'
            'E' -> 'G'
            else -> throw IllegalStateException()
        }
        val attackPower = 3
        val space get() = spaces[this.pos]!!

        val canAttack get() = this.space.adjacents.sortedBy { it.pos }.any { it.creature?.type == this.targetType }
        fun doAttack() { this.space.adjacents.sortedBy { it.pos }.filter { it.creature?.type == this.targetType }.minBy { it.creature!!.hp }!!.creature!!.hp -= this.attackPower }
    }

    val creatures = mutableListOf<Creature>()
    operator fun List<Creature>.get(type: Char): List<Creature> = creatures.filter { it.type == type && it.hp > 0 }
    operator fun List<Creature>.get(key: Point2D): Creature? = creatures.find { it.pos == key && it.hp > 0 }

    inner class OpenSpaceNode(val pos: Point2D){
        val adjacents = mutableListOf<OpenSpaceNode>()
        val adjacentPoints = mutableListOf<Point2D>()

        val north = pos+Point2D(0, -1)
        val east = pos+Point2D(1, 0)
        val west = pos+Point2D(-1, 0)
        val south = pos+Point2D(0, 1)

        val creature get() = creatures.find { it.pos == this.pos && it.hp > 0 }

        fun findPathTo(target: Point2D): List<Point2D>?{
            if (this.adjacentPoints.filter { spaces[it]!!.creature == null }.contains(target)){
                return listOf(target)
            }
            val openNodes = TreeSet<OpenSpaceNode>(compareBy<OpenSpaceNode> { it.pos.manhattanDistanceFrom(this.pos) }.thenComparing(
                compareBy { it.pos }))
            openNodes.addAll(adjacents.filter { it.creature == null })
            val addedBy = mutableMapOf<Point2D,Point2D>()
            adjacentPoints.forEach { addedBy[it] = this.pos }
            val closedNodes = mutableSetOf(this.pos)
            while (openNodes.isNotEmpty()){
                val testNode = openNodes.first()
                openNodes.remove(testNode)
                if (testNode.adjacentPoints.contains(target)){
                    val path = LinkedList<Point2D>()
                    path.add(target)
                    var p = testNode.pos
                    while (p != this.pos){
                        path.push(p)
                        p = addedBy[p]!!
                    }
                    return path
                } else {
                    closedNodes.add(testNode.pos)
                    testNode.adjacents.filter { !closedNodes.contains(it.pos) && it.creature == null }.sortedBy { it.pos }.forEach {newNode->
                        openNodes.add(newNode)
                        if (!addedBy.containsKey(newNode.pos) /*|| addedBy[newNode.pos]!!.manhattanDistanceFrom(target) >= testNode.pos.manhattanDistanceFrom(target)*/){
                            addedBy[newNode.pos] = testNode.pos
                        }
                    }
                }
            }
            return null
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as OpenSpaceNode

            if (pos != other.pos) return false

            return true
        }

        override fun hashCode(): Int {
            return pos.hashCode()
        }
    }

    val parser = { it:List<String>->
        it.mapIndexed { row,line ->line.mapIndexed { col,char-> when(char){
            '.' -> SquareType.Open
            'G' -> { creatures.add(Creature('G', Point2D(col,row))); SquareType.Open}
            'E' -> { creatures.add(Creature('E', Point2D(col,row))); SquareType.Open}
            '#' -> SquareType.Wall
            else->throw IllegalStateException(char.toString())
        } } }
    }
    /*
    #######
    #.G...#   G(200)
    #...EG#   E(200), G(200)
    #.#.#G#   G(200)
    #..G#E#   G(200), E(200)
    #.....#
    #######

     */

    val doTest = 1
    val testData = ("#######\n" +
            "#.G...#\n" +
            "#...EG#\n" +
            "#.#.#G#\n" +
            "#..G#E#\n" +
            "#.....#\n" +
            "#######").split("\n")
    val parsedLines = parser(when(doTest){
        0 -> lines
        else -> testData
    })

    val spaces = parsedLines.mapIndexed { row, r ->
        r.mapIndexedNotNull {
                col, c -> if (c == SquareType.Open) OpenSpaceNode(Point2D(col,row)) else null
        }
    }.flatten().groupBy { it.pos }.mapValues { it.value[0] }.also {nodes ->
        nodes.values.forEach { node->
            nodes[node.north]?.let { node.adjacents+= it; node.adjacentPoints += it.pos }
            nodes[node.east]?.let { node.adjacents+= it; node.adjacentPoints += it.pos }
            nodes[node.south]?.let { node.adjacents+= it; node.adjacentPoints += it.pos }
            nodes[node.west]?.let { node.adjacents+= it; node.adjacentPoints += it.pos }
        }
    }

    fun printGrid(){
        for (y in 0 until parsedLines.size){
            val hps = mutableListOf<Creature>()
            for (x in 0 until parsedLines[y].size){
                when (parsedLines[y][x]){
                    SquareType.Wall -> print('#')
                    SquareType.Open -> print(when(val c = spaces[Point2D(x,y)]!!.creature){
                        null -> '.'
                        else -> {hps.add(c); c.type}
                    })
                }
            }
            println("\t"+(hps.joinToString { "${it.type}:${it.hp}" }))
        }
        println("--------------------------------------------")
        System.out.flush()
    }

    override fun part1(): Any {
        var rounds = 0
        while (creatures['E'].isNotEmpty() && creatures['G'].isNotEmpty()){
            var validTargetFound = false
            creatures.filter { it.hp > 0 }.sortedBy { it.pos }.forEach { creature ->
                if (creature.hp <1)//died during round
                    return@forEach
                if (creature.canAttack ){
                    creature.doAttack()
                    validTargetFound = true
                } else {
                    val targets =
                        creatures[creature.targetType].flatMap { spaces[it.pos]!!.adjacents.filter { it.creature == null } }.sortedBy { it.pos }.mapNotNull { creature.space.findPathTo(it.pos) }
                            .groupBy { it.size }
                    if (targets.isNotEmpty()){
                        creature.pos = targets.entries.minBy { it.key }!!.value.sortedBy { it.last() }.first().first { it != creature.pos }
                        validTargetFound = true
                        if (creatures.filter { it.hp>0 }.groupBy { it.pos }.any { it.value.size > 1 }){
                            throw IllegalStateException()
                        }
                        if (creature.canAttack ) {
                            creature.doAttack()
                        }
                    }/* else{
                        val positions = creatures[creature.targetType].flatMap { spaces[it.pos]!!.adjacents.filter { it.creature == null } }
                        for (p in positions){
                            creature.space.findPathTo(p.pos)
                        }
                    }*/
                }
            }
            if (!validTargetFound || creatures['E'].isEmpty() || creatures['G'].isEmpty()){
                break//stalemate or all targets neutralised
            }
            rounds++
            //println("Round $rounds, ${creatures['E'].size} Elves, ${creatures['G'].size} Goblins remain")
            //printGrid()
        }
        println("Total Rounds $rounds, ${creatures['E'].size} Elves, ${creatures['G'].size} Goblins remain")
        printGrid()
        return (creatures['E'].sumBy { it.hp }+creatures['G'].sumBy { it.hp })*rounds
    }

    override fun part2(): Any {
        return "incomplete"
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day15().doParts()
        }
    }
}