package util

import kotlin.math.max

/**
 * Created by Thiakil on 13/12/2018.
 */
enum class CompassDirection4 {
    NORTH,EAST,SOUTH,WEST;

    fun rotateClockwise():CompassDirection4 = VALUES[(this.ordinal+1) % VALUES.size]
    fun rotateAntiClockwise():CompassDirection4 = VALUES[max((this.ordinal-1) % VALUES.size, 0)]

    companion object {
        val VALUES = CompassDirection4.values()
    }
}