package util

import java.lang.IllegalArgumentException

class BooleanOp<T : Comparable<T>> {
    private val ops = mapOf(
        ">" to { l: T, r: T -> l > r },
        "<" to { l: T, r: T -> l < r },
        "==" to { l: T, r: T -> l == r },
        "!=" to { l: T, r: T -> l != r },
        "<=" to { l: T, r: T -> l <= r },
        ">=" to { l: T, r: T -> l >= r }
    )

    @Suppress("UNCHECKED_CAST")
    companion object {
        private val INSTANCE = BooleanOp<Comparable<Any>>()
        fun <T : Comparable<T>> from(op: String): (T,T)->Boolean {
            if (INSTANCE.ops.containsKey(op)){
                return INSTANCE.ops[op] as (T,T)->Boolean
            }
            throw IllegalArgumentException("$op not implemented")
        }
    }
}