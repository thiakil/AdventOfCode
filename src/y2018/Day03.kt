package y2018

import util.Day
import util.Point2D
import util.Rectangle
import util.RectangleIntersection
import java.lang.Integer.parseInt

/**
 * Created by Thiakil on 1/12/2018.
 */

private class Day03 : Day(2018) {
    val exampledata = ("#1 @ 1,3: 4x4\n" +
            "#2 @ 3,1: 4x4\n" +
            "#3 @ 5,5: 2x2").split("\n")

    val pattern = Regex("#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)")
    val claims = lines.map { line ->
        val match = pattern.matchEntire(line)!!.groupValues
        Claim(parseInt(match[1]), parseInt(match[2]), parseInt(match[3]), parseInt(match[4]), parseInt(match[5]))
    }

    val intersects = mutableSetOf<ClaimIntersection>()
    val coordsCovered = mutableSetOf<Point2D>()
    val clashedClaims = mutableSetOf<Claim>()

    init {
        for (claim in claims) {
            for (testclaim in claims) {
                val intersect: ClaimIntersection = ClaimIntersection.from(claim, testclaim)
                if (claim == testclaim/* || intersects.contains(intersect)*/) {
                    continue
                }
                if (intersect.r.hasOverlap) {
                    clashedClaims.add(claim)
                    clashedClaims.add(testclaim)
                    intersects.add(intersect)
                    coordsCovered.addAll(intersect.r.getOverlapArea())
                }
            }
        }
    }

    override fun part1(): String {

        return coordsCovered.size.toString()
    }

    override fun part2(): String {
        return claims.find { claim -> !clashedClaims.contains(claim) }?.claimId?.toString() ?: "failed"
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day03().doParts()
        }
    }

}

private data class Claim(val claimId: Int, val area: Rectangle) {
    constructor(claimId: Int, x: Int, y: Int, width: Int, height: Int) : this(claimId,
        Rectangle(x, y, width, height)
    )
}

private data class ClaimIntersection(val claim1: Claim, val claim2: Claim) {
    val r = RectangleIntersection.from(claim1.area, claim2.area)

    companion object {
        fun from(claim1: Claim, claim2: Claim) = if (claim1.claimId < claim2.claimId) {
            ClaimIntersection(claim1, claim2)
        } else {
            ClaimIntersection(claim2, claim1)
        }
    }
}