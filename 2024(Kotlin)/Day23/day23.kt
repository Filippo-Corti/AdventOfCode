import java.io.File

val lan = mutableMapOf<String, HashSet<String>>()

fun main() {

    File("input.txt").readLines()
        .map { it.split("-") }
        .forEach {
            val set1 = lan.getOrPut(it[0]) { hashSetOf() }
            val set2 = lan.getOrPut(it[1]) { hashSetOf() }
            set1.add(it[1])
            set2.add(it[0])
        }

    println(part1())
    println(part2())
}

// A Clique is a subset of vertices of an undirected graph such that every two distinct vertices in the clique are adjacent.
// That is, a Clique of a Graph G is an induced subgraph of G that is complete.

// The Bron–Kerbosch algorithm is an enumeration algorithm for finding all maximal cliques in an undirected graph.

fun bronKerbosch(
    R: MutableSet<String> = mutableSetOf<String>(), // Current Clique
    P: MutableSet<String>, // Potential Vertices (all Vertices of the considered SubGraph)
    X: MutableSet<String> = mutableSetOf<String>(), // Excluded Vertices
) : List<Set<String>> {
    if (P.isEmpty() && X.isEmpty()) {
        return listOf(R)
    }
    
    val result = mutableListOf<Set<String>>()
    val Pcopy = P.toSet()
    for (v in Pcopy) {
        val neighbours = lan[v] ?: emptySet()
        result += bronKerbosch(
            (R + v).toMutableSet(),
            P.intersect(neighbours).toMutableSet(),
            X.intersect(neighbours).toMutableSet(),
        )
        P -= v
        X += v
    }

    return result
}

fun <T> Iterable<T>.combinations(length : Int) : Sequence<List<T>> = sequence {
    val list = this@combinations.toList()
    if (length == 0) {
        yield(listOf())
    } else {
        for (i in list.indices) {
            val element = list[i]
            val remaining = list.subList(i + 1, list.size)
            for (combination in remaining.combinations(length - 1)) {
                yield(listOf(element) + combination)
            }
        }
    }
}

fun part1() : Int {
    return bronKerbosch( P = lan.keys.toMutableSet() )
        .filter { it.size >= 3 }
        .flatMap { it.combinations(3) }
        .filter { it.find { it[0] == 't' } != null }
        .map { it.sorted() }
        .toHashSet()
        .size
}

fun part2() : String {
    return bronKerbosch( P = lan.keys.toMutableSet() )
        .maxBy { it.size }
        .sorted()
        .joinToString(",")
}
