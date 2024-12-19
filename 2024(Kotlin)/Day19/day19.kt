import java.io.File

var rugs : List<String> = listOf()

fun main() {

    val lines = File("input.txt").readLines()
    rugs = lines[0].split(", ")
    val designs : List<String> = lines.subList(2, lines.size)

    println(part1(designs))
    println(part2(designs))
}

val map = mutableMapOf<String, Long>()

fun countPossibleWays(design : String) : Long {
    if (map.containsKey(design)) {
        return map.getValue(design)
    }

    if (design == "") {
        map[design] = 1L
        return 1L
    }

    var count = 0L

    for (rug in rugs) {
        if (design.startsWith(rug)) {
            count += countPossibleWays(design.substring(rug.length))
        }
    }
    
    map[design] = count
    return count
}

fun part1(designs : List<String>) : Int {
    return designs.filter { countPossibleWays(it) > 0L }.size
}

fun part2(designs : List<String>) : Long {
    return designs
        .map { countPossibleWays(it) }
        .sum() 
}