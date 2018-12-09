package y2018

import util.Day
import java.lang.IllegalStateException

/**
 * Created by Thiakil on 1/12/2018.
 */

class Day04 : Day(2018){
    val guardShift = Regex("\\[(\\d{4})-(\\d{2})-(\\d{2}) (\\d{2}):(\\d{2})] Guard #(\\d+) begins shift")
    val guardSleep = Regex("\\[(\\d{4})-(\\d{2})-(\\d{2}) (\\d{2}):(\\d{2})] falls asleep")
    val guardWake = Regex("\\[(\\d{4})-(\\d{2})-(\\d{2}) (\\d{2}):(\\d{2})] wakes up")

    val parsed = lines.sorted()
    val guards = mutableMapOf<Int, Guard>()

    init {
        var currentGuard: Guard? = null
        var currentSleepStart: Int? = null
        for (line in parsed) {
            if (guardShift.matches(line)) {
                val id = guardShift.matchEntire(line)!!.groupValues[6]
                currentGuard = guards.computeIfAbsent(id.toInt()) { Guard(id.toInt()) }
            } else if (currentGuard != null) {
                if (guardSleep.matches(line) && currentSleepStart == null) {
                    currentSleepStart = guardSleep.matchEntire(line)!!.groupValues[5].toInt()
                } else if (guardWake.matches(line) && currentSleepStart != null) {
                    val sleepEnd = guardWake.matchEntire(line)!!.groupValues[5].toInt()
                    currentGuard.totalSleep += sleepEnd - currentSleepStart
                    for (min in currentSleepStart until sleepEnd) {
                        currentGuard.minsAsleep[min]++
                    }
                    currentSleepStart = null
                } else {
                    throw IllegalStateException(line)
                }
            } else {
                throw IllegalStateException(line)
            }
        }
    }

    override fun part1(): String {

        val sleepiestGuard = guards.values.maxBy { it.totalSleep }!!
        val commonMinute = sleepiestGuard.sleepiestMinute()

        return (sleepiestGuard.id * commonMinute).toString()
    }

    override fun part2(): String {
        val sleepiestGuard = guards.values.maxBy { it.minsAsleep.max()!! }!!
        val commonMinute = sleepiestGuard.sleepiestMinute()

        return (sleepiestGuard.id * commonMinute).toString()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day04().doParts()
        }
    }
}

data class Guard(val id: Int) {
    var totalSleep: Int = 0
    val minsAsleep: Array<Int> = Array(60){0}

    fun sleepiestMinute(): Int = minsAsleep.indexOf(minsAsleep.max()!!)
}