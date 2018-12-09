package y2017
import util.*
import kotlin.math.absoluteValue
import kotlin.math.sign

//https://www.redblobgames.com/grids/hexagons/#coordinates
private class Day11 : util.Day(2017) {

    val directions = mapOf<String, Point3D>(
        "n" to Point3D(0,1, -1),
        "ne" to Point3D(1,0,-1),
        "se" to Point3D(1,-1,0),
        "s" to Point3D(0,-1,1),
        "sw" to Point3D(-1,0,1),
        "nw" to Point3D(-1,1,0)
    )

    val parser = { it: List<String> ->
        it.first().split(",").map { directions[it]!! }
    }

    val doTest = 0
    val testData = ("ne,ne,sw,sw").split("\n")
    val parsedLines = parser(
        when (doTest) {
            0 -> lines
            else -> testData
        }
    )

    var maxDist = 0

    val target = parsedLines.reduce { acc, point3D ->
        val curpos = acc+point3D
        maxDist = Math.max(maxDist, distanceFromStart(curpos))
        curpos
    }

    fun distanceFromStart(target: Point3D):Int {
        if (0 == target.x && target.y == 0 && target.z == 0)
            return 0
        val overallDir = directions.entries.find {
            (it.value.x.sign == target.x.sign && it.value.y.sign == target.y.sign) ||
                    (it.value.x.sign == target.x.sign && it.value.z.sign == target.z.sign) ||
                    (it.value.y.sign == target.y.sign && it.value.z.sign == target.z.sign)
        }!!
        val minVal = target.getMin(overallDir.value)
        val remaining = target - Point3D(minVal * overallDir.value.x.sign, minVal * overallDir.value.y.sign , minVal * overallDir.value.z.sign)

        return minVal + Math.max(remaining.x.absoluteValue, Math.max(remaining.y.absoluteValue, remaining.z.absoluteValue))
    }

    override fun part1(): Any = distanceFromStart(target)

    override fun part2(): Any = maxDist

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day11().doParts()
        }
    }

    fun Point3D.getMin(axis: Point3D): Int{
        return if (axis.x == 0){
            Math.min(this.y*axis.y, this.z*axis.z)
        } else if (axis.y == 0){
            Math.min(this.x*axis.x, this.z*axis.z)
        } else /*if (axis.z == 0)*/{
            Math.min(this.y*axis.y, this.x*axis.x)
        }
    }
}