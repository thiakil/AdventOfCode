package util

/**
 * Created by Thiakil on 6/12/2018.
 */

data class Rectangle(val x: Int, val y: Int, val width: Int, val height: Int){
    constructor(point1: Point2D, point2: Point2D):
            this(Math.min(point1.x, point2.x),
                Math.min(point1.y, point2.y),
                Math.max(point1.x, point2.x)-Math.min(point1.x, point2.x),
                Math.max(point1.y, point2.y)-Math.min(point1.y, point2.y))
    val y2 = y+height
    val x2 = x+width
    val area = width*height
}

data class RectangleIntersection(val rectangle1: Rectangle, val rectangle2: Rectangle){
    val x2 = Math.min(rectangle2.x2, rectangle1.x2)
    val x = Math.max(rectangle1.x, rectangle2.x)
    val y2 = Math.min(rectangle1.y2, rectangle2.y2)
    val y = Math.max(rectangle1.y, rectangle2.y)

    val x_overlap = Math.max(0, x2 - x)
    val y_overlap = Math.max(0, y2 - y)

    val hasOverlap = x_overlap > 0 && y_overlap > 0

    fun getOverlapRange(): RangePoint2D = Point2D(x, y)..Point2D(x2 - 1, y2 - 1)

    fun getOverlapArea():Set<Point2D> {
        val coords = mutableSetOf<Point2D>()
        for (i in x until x2){
            for (j in y until y2){
                coords.add(Point2D(i, j))
            }
        }
        return coords
    }

    companion object {
        fun from(rectangle1: Rectangle, rectangle2: Rectangle) =
            RectangleIntersection(rectangle1, rectangle2)
    }
}