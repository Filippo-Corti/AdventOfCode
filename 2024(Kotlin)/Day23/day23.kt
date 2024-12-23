import java.io.File

fun main() {

    val lan = mutableMapOf<String, HashSet<String>>()

    File("input.txt").readLines()
        .map { it.split("-") }
        .forEach {
            val set1 = lan.getOrPut(it[0]) { hashSetOf() }
            val set2 = lan.getOrPut(it[1]) { hashSetOf() }
            set1.add(it[1])
            set2.add(it[0])
        }

    println(part1(lan))
}

fun getTriples(lan : Map<String, HashSet<String>>) : HashSet<List<String>> {
    val triples = hashSetOf<List<String>>()
    for ((c1, connections) in lan) {
        for (c2 in connections) {
            val int = lan[c1]!!.intersect(lan[c2]!!)
            for (c3 in int) {
                val c3conns = lan[c3]!!
                if (c3conns.contains(c1) && c3conns.contains(c2)) {
                    val triple = listOf(c1, c2, c3).sorted()
                    triples.add(triple)
                } 
            }
        }
        val triple = mutableListOf<String>()

    }
    return triples
}

fun part1(lan : Map<String, HashSet<String>>) : Int {
    return getTriples(lan).filter { it.find { it[0] == 't' } != null }.size
}