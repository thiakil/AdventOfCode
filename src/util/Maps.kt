package util

class REHelper(private val match: MatchNamedGroupCollection){
    operator fun get(key: String):String = match[key]!!.value
    fun getNullable(key: String):String? = match[key]?.value
    operator fun get(key: Int):String = match[key]!!.value
    fun getNullable(key: Int):String? = match[key]?.value
}

fun <T> String.map(re: Regex, transformer: (REHelper)->T):T{
    return transformer(REHelper(re.matchEntire(this)?.groups as MatchNamedGroupCollection? ?: throw IllegalArgumentException("did not match $this")))
}

fun <T> Iterable<String>.map(re: Regex, transformer: (REHelper)->T):List<T>{
    return this.map { it.map(re, transformer) }
}

class ComputedMap<K,V>(val mappingFunction: (key:K)->V): HashMap<K,V>(){
    override operator fun get(key: K):V = this.computeIfAbsent(key, mappingFunction)
}