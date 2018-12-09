package util

/**
 * Created by Thiakil on 6/12/2018.
 */

class CountingMap<T>(val initialVal:Int=0): HashMap<T,Int>() {
    operator fun plusAssign(obj:T){
        this.compute(obj) {_,v-> when (v){
            null -> initialVal+1
            else -> v+1
        }}
    }

    override operator fun get(key: T): Int {
        return super.getOrDefault(key, initialVal)
    }
}