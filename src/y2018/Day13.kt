package y2018

import util.*

private class Day13 : Day(2018){

    enum class TrackType{
        NONE, VERTICAL, HORIZONTAL, CORNER_TOP_LEFT, CORNER_TOP_RIGHT, CORNER_BOTTOM_LEFT, CORNER_BOTTOM_RIGHT, JUNCTION
    }

    enum class Turns{
        LEFT,STRAIGHT,RIGHT
    }

    data class Cart(var location: Point2D, var direction: CompassDirection4, var movedThisTick: Boolean = false){
        constructor(x: Int, y:Int, direction: CompassDirection4): this(Point2D(x, y), direction)
        var junctionTurns = 0

        fun advance(tracks: List<List<TrackType>>){
            location += when(tracks[this.location.y][this.location.x]){
                TrackType.VERTICAL,TrackType.HORIZONTAL -> direction.getChange()
                TrackType.NONE -> throw IllegalStateException("cart track location")
                TrackType.CORNER_TOP_LEFT -> when(direction){
                    CompassDirection4.NORTH -> {direction = CompassDirection4.EAST; direction.getChange()}
                    CompassDirection4.WEST -> {direction = CompassDirection4.SOUTH; direction.getChange()}
                    else -> throw IllegalStateException()
                }
                TrackType.CORNER_TOP_RIGHT -> when (direction){
                    CompassDirection4.EAST -> {direction = CompassDirection4.SOUTH; direction.getChange()}
                    CompassDirection4.NORTH -> {direction = CompassDirection4.WEST; direction.getChange()}
                    else -> throw IllegalStateException()
                }
                TrackType.CORNER_BOTTOM_LEFT -> when(direction){
                    CompassDirection4.SOUTH -> {direction = CompassDirection4.EAST; direction.getChange()}
                    CompassDirection4.WEST -> {direction = CompassDirection4.NORTH; direction.getChange()}
                    else -> throw IllegalStateException()
                }
                TrackType.CORNER_BOTTOM_RIGHT -> when(direction){
                    CompassDirection4.SOUTH -> {direction=CompassDirection4.WEST; direction.getChange()}
                    CompassDirection4.EAST -> {direction= CompassDirection4.NORTH; direction.getChange()}
                    else -> throw IllegalStateException()
                }
                TrackType.JUNCTION -> {
                    val thisTurn = junctionTurns; junctionTurns = (junctionTurns+1)%3
                    when(Turns.values()[thisTurn]){
                        Turns.LEFT -> {direction = direction.rotateAntiClockwise(); direction.getChange()}
                        Turns.RIGHT -> {direction = direction.rotateClockwise(); direction.getChange()}
                        else -> direction.getChange()
                    }
                }
            }

        }
        fun CompassDirection4.getChange(): Point2D = when(this){
            CompassDirection4.NORTH -> Point2D(0,-1)
            CompassDirection4.SOUTH -> Point2D(0,1)
            CompassDirection4.EAST -> Point2D(1,0)
            CompassDirection4.WEST-> Point2D(-1,0)
        }

        fun copy():Cart = Cart(this.location, this.direction)
    }

    val parser = { it:List<String>->
        val carts = mutableListOf<Cart>()
        val tracks = mutableListOf<MutableList<TrackType>>()
        it.forEachIndexed { row, s ->
            val curRow = mutableListOf<TrackType>()
            tracks.add(curRow)
            s.forEachIndexed { col, c ->
                curRow.add(when(c){
                    ' ' -> TrackType.NONE
                    '|' -> TrackType.VERTICAL
                    '-' -> TrackType.HORIZONTAL
                    '/' -> if(col > 0 &&(curRow[col-1] == TrackType.HORIZONTAL || curRow[col-1] == TrackType.JUNCTION)) TrackType.CORNER_BOTTOM_RIGHT else TrackType.CORNER_TOP_LEFT
                    '\\' -> if(col > 0 &&(curRow[col-1] == TrackType.HORIZONTAL || curRow[col-1] == TrackType.JUNCTION)) TrackType.CORNER_TOP_RIGHT else TrackType.CORNER_BOTTOM_LEFT
                    '+' -> TrackType.JUNCTION
                    '^' -> {carts.add(Cart(col, row, CompassDirection4.NORTH)); TrackType.VERTICAL}
                    'v' -> {carts.add(Cart(col, row, CompassDirection4.SOUTH)); TrackType.VERTICAL}
                    '>' -> {carts.add(Cart(col, row, CompassDirection4.EAST)); TrackType.HORIZONTAL}
                    '<' -> {carts.add(Cart(col, row, CompassDirection4.WEST)); TrackType.HORIZONTAL}
                    else-> throw IllegalStateException("unknown char $c")
                })
            }
            var idx = 0
            val rowStr = curRow.joinToString("") {
                val i = idx++
                return@joinToString when (val c = carts.find{ it.location == Point2D(i, row)}){
                    null -> when(it){
                        TrackType.NONE-> " "
                        TrackType.VERTICAL -> "|"
                        TrackType.HORIZONTAL -> "-"
                        TrackType.CORNER_TOP_LEFT -> "/"
                        TrackType.CORNER_TOP_RIGHT -> "\\"
                        TrackType.CORNER_BOTTOM_LEFT -> "\\"
                        TrackType.CORNER_BOTTOM_RIGHT -> "/"
                        TrackType.JUNCTION -> "+"
                    }
                    else -> when (c.direction){
                        CompassDirection4.NORTH -> "^"
                        CompassDirection4.EAST -> ">"
                        CompassDirection4.SOUTH -> "v"
                        CompassDirection4.WEST -> "<"
                    }
            } }
            if (s != rowStr)
                throw IllegalStateException("parse fail\n$s\n$rowStr")
        }
        Pair(carts, tracks)
    }

    val doTest = 0
    val testData = ("/->-\\        \n" +
            "|   |  /----\\\n" +
            "| /-+--+-\\  |\n" +
            "| | |  | v  |\n" +
            "\\-+-/  \\-+--/\n" +
            "  \\------/   ").split("\n")
    val parsedLines = parser(when(doTest){
        0 -> lines
        else -> testData
    })
    val carts = parsedLines.first
    val tracks = parsedLines.second

    override fun part1(): Any {
        val myCarts = carts.map { it.copy() }.toMutableList()
        var iters = 0
        while (true){
            myCarts.sortBy { it.location }
            myCarts.forEach { it.movedThisTick = false }
            iters++
            for (cart in myCarts){
                cart.advance(tracks)
                if (myCarts.any { it.movedThisTick && it !== cart && it.location== cart.location }){
                    println(iters)
                    return cart.location
                }
                cart.movedThisTick = true
            }
            /*val crashed = myCarts.filter { cart -> myCarts.any { it !== cart && it.location== cart.location } }.sortedBy { it.location }
            if (crashed.isNotEmpty()){
                crashed.forEach { println(it)}
                return crashed.first().location
            }*/
        }
    }

    override fun part2(): Any {
        return "incomplete"
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day13().doParts()
        }
    }
}