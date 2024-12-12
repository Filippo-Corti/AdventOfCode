import java.io.File

val dirs = listOf(
    0 to 1,
    0 to -1,
    1 to 0,
    -1 to 0
)

val dirs2 = listOf(
    0 to 1,
    0 to -1,
    1 to 0,
    -1 to 0,
    -1 to -1,
    1 to 1,
    1 to -1,
    -1 to 1
)

var rows : Int = 0
var cols : Int = 0

fun main() {
    
    val map : MutableMap<Pair<Int, Int>, Char> = mutableMapOf()

    val lines = File("input.txt").readLines()
    lines.forEachIndexed { row, line ->
        line.forEachIndexed{ col, cell ->
            map.put(row to col, cell)
        }
    } 

    rows = lines.size
    cols = lines[0].length


    //println(part1(map))
    println(part2(map))
}

var visited : HashSet<Pair<Int, Int>> = HashSet()

fun part1(map : MutableMap<Pair<Int, Int>, Char>) : Int {
    var score = 0
    for (cell in map.keys) {
        if (!visited.contains(cell)) {
            score += findRegion(map, cell)
        }
    }
    return score
}

fun findRegion(map : MutableMap<Pair<Int, Int>, Char>, cell : Pair<Int, Int>) : Int {
    val filled : HashSet<Pair<Int, Int>> = HashSet() 
    val perimeter = floodFill(map, cell, filled)
    return perimeter * filled.size
}

fun floodFill(map : MutableMap<Pair<Int, Int>, Char>, curr : Pair<Int, Int>, filled : HashSet<Pair<Int, Int>>) : Int {
    filled.add(curr)
    visited.add(curr)
    var perimeter = 0
    for ((dr, dc) in dirs) {
        val next = Pair(curr.first + dr, curr.second + dc)

        if (filled.contains(next)) 
            continue
        else if (next.first < 0 || next.first >= rows || next.second < 0 || next.second >= cols) 
            perimeter++
        else if (map[next] != map[curr]) 
            perimeter++
        else 
            perimeter += floodFill(map, next, filled)
    }
    return perimeter
} 
