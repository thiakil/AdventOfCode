package util

data class Point3D(val x: Int, val y: Int, val z: Int): Comparable<Point3D>{
    override fun compareTo(other: Point3D): Int = when (val resX = Integer.compare(this.y, other.y)){
        0 -> when(val resY = Integer.compare(this.x, other.x)){
            0 -> Integer.compare(this.z, other.z)
            else -> resY
        }
        else -> resX
    }

    operator fun plus(other: Point3D): Point3D = Point3D(this.x + other.x, this.y + other.y, this.z+other.z)
    operator fun minus(other: Point3D): Point3D = Point3D(this.x - other.x, this.y - other.y, this.z-other.z)

    companion object {
        val ZERO = Point2D(0, 0)
    }
}
