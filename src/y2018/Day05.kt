package y2018

import util.Day
import java.util.*

/**
 * Created by Thiakil on 1/12/2018.
 */

private fun MutableList<PUnit>.fullyReact(): MutableList<PUnit>{
    var reactedUnits = this
    var lastRun: MutableList<PUnit>
    do {
        lastRun = reactedUnits
        reactedUnits = LinkedList()
        for (current in lastRun) {
            if (reactedUnits.isEmpty()) {
                reactedUnits.push(current)
            } else {
                val previous = reactedUnits.peekLast()

                if (previous.reacts(current)) {
                    reactedUnits.removeLast()
                } else {
                    reactedUnits.addLast(current)
                }
            }
        }
    } while (reactedUnits.size != lastRun.size)
    return reactedUnits
}

class Day05 : Day(2018){

    val units= lines.first().toCharArray().mapTo(LinkedList()){ PUnit.from(it) }

    private val p1 = units.fullyReact()

    override fun part1(): String {
        return p1.size.toString()
    }

    override fun part2(): String {
        return p1.map { it.type }.distinct().map { type -> units.filter { it.type != type }.toMutableList().fullyReact().size }.min().toString()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day05().doParts()
        }
    }

}

enum class Polarity{
    UPPER,LOWER
}

data class PUnit(val type: Char, val polarity: Polarity){
    fun reacts(other: PUnit): Boolean = this.type == other.type && this.polarity != other.polarity

    companion object {
        fun from(char: Char): PUnit {
            return when (char){
                in 'a'..'z'-> PUnit(char, Polarity.LOWER)
                in 'A'..'Z' -> PUnit(char.toLowerCase(), Polarity.UPPER)
                else -> throw IllegalArgumentException("Bad char $char")
            }
        }
    }
}