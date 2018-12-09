package util

import java.io.FileNotFoundException
import java.util.*

/**
 * Created by Thiakil on 1/12/2018.
 */
abstract class Day(val year: Int) {
    val start = System.currentTimeMillis()

    abstract fun part1(): Any
    abstract fun part2(): Any

    fun doParts() {
        val construction = System.currentTimeMillis()
        println("Part 1: ${part1()}")
        val mid = System.currentTimeMillis()
        println("Part 2: ${part2()}")
        val end = System.currentTimeMillis()
        println("\nTook ${construction-start}ms for construction ${mid-construction}ms for part 1 and ${end-mid}ms for part 2, ${end-start}ms total")
    }

    protected open val filename: String get() = javaClass.simpleName!!.toLowerCase(Locale.ROOT)

    @Suppress("LeakingThis")
    val lines: List<String> = javaClass.getResourceAsStream("/$year/${this.filename}.txt")?.bufferedReader()?.use { reader ->
        reader.readLines()
    } ?: throw FileNotFoundException(filename)

}

