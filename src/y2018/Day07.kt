package y2018

import java.util.*
import util.*

private class Day07 : Day(2018){
    val RE = Regex("Step (?<dep>[A-Z]) must be finished before step (?<step>[A-Z]) can begin.")

    val parser = { it:List<String>->
        it.map { StepLine(RE.matchEntire(it)!!.groups as MatchNamedGroupCollection) }.groupBy { it.step }.mapValues { e-> e.value.map { it.dep } }
    }

    val test = lines.map(RE) { StepLine(it["dep"][0], it["step"][0]) }

    val doTest = 0
    val testData = ("Step C must be finished before step A can begin.\n" +
            "Step C must be finished before step F can begin.\n" +
            "Step A must be finished before step B can begin.\n" +
            "Step A must be finished before step D can begin.\n" +
            "Step B must be finished before step E can begin.\n" +
            "Step D must be finished before step E can begin.\n" +
            "Step F must be finished before step E can begin.").split("\n")
    val parsedLines = parser(when(doTest){
        0 -> lines
        else -> testData
    })


    val stepList = ArrayList<Char>(26)

    override fun part1(): Any {
        val remainingSteps = LinkedList<Char>(('A'..'Z').toList())
        while (remainingSteps.peekFirst() != null){
            val iter = remainingSteps.iterator()
            while(iter.hasNext()){
                val test = iter.next()
                val deps = parsedLines[test]
                if (deps == null || deps.none { remainingSteps.contains(it) }){
                    iter.remove()
                    stepList.add(test)
                    break
                }
            }
        }
        return stepList.joinToString(separator = "")
    }

    override fun part2(): Any {
        val elves = Array(5){ Elf() }
        val steps = ('A'..'Z').toList().map { Step(it, parsedLines[it]?.toTypedArray()) }.groupBy { it.id }.mapValues { it.value[0] }
        var doneSteps = 0
        var time = 0
        val neededSteps = steps.entries.size
        while(doneSteps < neededSteps){
            time++

            val availJobs = steps.values.filterTo(LinkedList()) { s-> !s.assigned && !s.done && (s.deps == null || s.deps.all {steps[it]!!.done})}
            availJobs.sort()
            for (elf in elves){
                if (elf.currentJob == null){
                    if (availJobs.size > 0){
                        val j = availJobs.removeFirst()
                        elf.currentJob = j.id
                        elf.timeLeft = j.duration-1
                        j.assigned = true
                    }
                } else {
                    elf.timeLeft--
                    if (elf.timeLeft <= 0) {
                        steps[elf.currentJob!!]!!.done = true
                        println("Done ${steps[elf.currentJob!!]!!}")
                        elf.currentJob = null

                        doneSteps++
                    }
                }
            }
        }
        return time
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day07().doParts()
        }
    }
}

private data class Step(val id: Char, val deps: Array<Char>?): Comparable<Step>{
    val duration = 61+(id-'A')
    var done = false
    var assigned = false
    override fun compareTo(other: Step): Int = Character.compare(this.id, other.id)
}

private data class StepLine(val dep: Char, val step: Char){
    constructor(vals: List<String>): this(vals[1][0], vals[2][0])
    constructor(vals: MatchNamedGroupCollection): this(vals["dep"]!!.value[0], vals["step"]!!.value[0])
}

private data class Elf(var currentJob: Char? = null, var timeLeft:Int = 0)