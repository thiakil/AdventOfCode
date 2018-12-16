package y2017

import util.RepetitionResult
import util.findFirstRepetitionState

/*
s1, a spin of size 1: eabcd.
x3/4, swapping the last two programs: eabdc.
pe/b, swapping programs e and b: baedc.
 */

private class Day16 : util.Day(2017){

    class Dancer(var id: String){
        var next: Dancer? = null
        var prev: Dancer? = null
        val idx: Int get() = if (prev == null) 0 else prev!!.idx+1

        fun dancersToString(): String {
            val buffer = StringBuilder()
            var curDancer: Dancer? = this
            while (curDancer != null) {
                buffer.append(curDancer.id)
                curDancer = curDancer.next
            }

            return buffer.toString()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Dancer

            if (id != other.id) return false
            if (next?.id != other.next?.id) return false
            if (prev?.id != other.prev?.id) return false

            return true
        }

        override fun hashCode(): Int {
            var result = id.hashCode()
            result = 31 * result + (next?.id?.hashCode() ?: 0)
            result = 31 * result + (prev?.id?.hashCode() ?: 0)
            return result
        }


    }

    interface Move {
        fun dance(headDancer: Dancer, dancers: List<Dancer>):Dancer

        fun doSwap(dA: Dancer,dB: Dancer,dancers: List<Dancer>)/*: Dancer*/ {
            /*val tmpA = Pair(dA.next?.id, dA.prev?.id)
            val tmpB = Pair(dB.next?.id, dB.prev?.id)
            dA.next = dancers[tmpB.first]
            dB.next = dancers[tmpA.first]
            dA.prev = dancers[tmpB.second]
            dB.prev = dancers[tmpA.second]
            if (dA.prev != null){
                dA.prev!!.next = dA
            }
            if (dB.prev != null){
                dB.prev!!.next = dB
            }
            if (dA.next != null){
                dA.next!!.prev = dA
            }
            if (dB.next != null){
                dB.next!!.prev = dB
            }
            val newHead = dancers.values.find { it.prev == null }!!
            if (dancers.values.filter { it.prev==null }.count() != 1 || dancers.values.filter { it.next==null }.count() != 1 || newHead.dancersToString().length != 16){
                throw IllegalStateException()
            }
            return newHead*/
            val tmp = dA.id
            dA.id = dB.id
            dB.id = tmp
            /*dancers[dA.id] = dA
            dancers[dB.id] = dB*/
            //return dancers.values.find { it.prev == null }!!
        }
    }

    class Spin(val size:Int):Move{
        override fun dance(
            headDancer: Dancer,
            dancers: List<Dancer>
        ):Dancer {
            val lastDancer = dancers.find { it.next == null }!!
            var newLast = lastDancer
            for (i in 0 until size){
                newLast = newLast.prev!!
            }
            val newHead = newLast.next!!
            headDancer.prev = lastDancer
            lastDancer.next = headDancer
            newLast.next = null
            newHead.prev = null
            return newHead
        }
    }

    class Exchange(val a:Int, val b:Int): Move {
        override fun dance(
            headDancer: Dancer,
            dancers: List<Dancer>
        ):Dancer {
            val dA = getById(a, headDancer)
            val dB = getById(b, headDancer)

            doSwap(dA, dB, dancers)
            return headDancer
        }

        fun getById(idx: Int, headDancer: Dancer):Dancer{
            if (idx == 0)
                return headDancer
            var curreDancer = headDancer
            for (i in 1 .. idx){
                curreDancer = curreDancer.next!!
            }
            return curreDancer
        }
    }

    class Swap(val a: String, val b: String):Move{
        override fun dance(
            headDancer: Dancer,
            dancers: List<Dancer>
        ):Dancer {
            doSwap(dancers.find { it.id == a }!!, dancers.find { it.id == b }!!, dancers)
            return headDancer//
        }
    }

    val parser = { it:List<String>->
        it.first().split(",").map { when (it[0]){
            's' -> Spin(it.substring(1).toInt())
            'x' -> Exchange(it.substring(1, it.indexOf('/')).toInt(), it.substring(it.indexOf('/')+1).toInt())
            'p' -> Swap(it[1].toString(), it[3].toString())
            else -> throw IllegalStateException()
        } }
    }

    val doTest = 0
    val testData = ("").split("\n")
    val parsedLines = parser(when(doTest){
        0 -> lines
        else -> testData
    })

    override fun part1(): Any {
        //val dancers = mutableMapOf<String,Dancer>()
        val dancers = mutableListOf<Dancer>()
        var lastDancer: Dancer? = null
        for (id in 'a'..'p'){
            val newD = Dancer(id.toString())
           //dancers[id.toString()] = newD
            dancers.add(newD)
            newD.prev = lastDancer
            if (lastDancer != null)
                lastDancer.next = newD
            lastDancer = newD
        }
        var headDancer = dancers.first()
        for (m in parsedLines){
            headDancer = m.dance(headDancer, dancers)
        }
        return headDancer.dancersToString()
    }

    override fun part2(): Any {
        //val dancers = mutableMapOf<String,Dancer>()
        val dancers = mutableListOf<Dancer>()
        var lastDancer: Dancer? = null
        for (id in 'a'..'p'){
            val newD = Dancer(id.toString())
            //dancers[id.toString()] = newD
            dancers.add(newD)
            newD.prev = lastDancer
            if (lastDancer != null)
                lastDancer.next = newD
            lastDancer = newD
        }
        var headDancer = dancers.first()
        val sequence = mutableListOf(headDancer.dancersToString())
        val seen = hashSetOf(sequence.first())
        var iters = 0
        for (i in 1..1000000000) {
            if (i % 1000 == 0)
                println(i)
            for (m in parsedLines) {
                headDancer = m.dance(headDancer, dancers)
            }
            val newOrder = headDancer.dancersToString()
            if (!seen.add(newOrder)){
                break
            }
            sequence.add(newOrder)
            iters = i
        }

        return sequence[1000000000 % sequence.size-1]
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day16().doParts()
        }
    }
}