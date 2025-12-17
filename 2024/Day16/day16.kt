import java.io.File
import java.util.PriorityQueue

data class Position(val pos : Pair<Int, Int>, val dir : Pair<Int, Int>)

val dirs = listOf(
    0 to 1,
    0 to -1,
    1 to 0,
    -1 to 0
)

var rows : Int = 0
var cols : Int = 0

fun neighbours(pos : Pair<Int, Int>, map : Map<Pair<Int, Int>, Char>) : List<Position> {
    return dirs
        .map { Position(Pair(pos.first + it.first, pos.second + it.second), it) }
        .filter { map.containsKey(it.pos) && map[it.pos] != '#' }
}

fun main() {

    val map : MutableMap<Pair<Int, Int>, Char> = mutableMapOf()
    var guard = 0 to 0
    
    val lines = File("input.txt").readLines()
    lines.forEachIndexed { row, line ->
        line.forEachIndexed{ col, cell ->
            map.put(row to col, cell)
        }
    } 

    rows = lines.size
    cols = lines[0].length

    println(part1(map.toMap()))
    println(part2(map.toMap()))
}


fun part1(map : Map<Pair<Int, Int>, Char>) : Int {
    val start = Position(rows-2 to 1, 0 to 1)
    val end = 1 to cols-2
    return dijkstraFrom(start, map)
        .first
        .filter {(k, _) -> k.pos == end}
        .values
        .min()
}

fun part2(map : Map<Pair<Int, Int>, Char>) : Int {
    val start = Position(rows-2 to 1, 0 to 1)
    val end = 1 to cols-2

    val (dists, paths) = dijkstraFrom(start, map)
    val endWithDir = dists
        .filter {(k, _) -> k.pos == end}
        .minByOrNull { it.value}
        ?.key

    val shortestPathsToEnd = paths[endWithDir]!!
  
    val sittingTiles = hashSetOf<Pair<Int, Int>>()

    shortestPathsToEnd.forEach {
        sittingTiles.addAll(it.map {it.pos})
    }

    return sittingTiles.size
}

fun dijkstraFrom(start : Position, map : Map<Pair<Int, Int>, Char>) : Pair<Map<Position, Int>, Map<Position, MutableList<HashSet<Position>>>> {

    val priorityQueue = PriorityQueue<Pair<Position, Int>>( compareBy { it.second })
    val distances = mutableMapOf<Position, Int>().withDefault { Int.MAX_VALUE }
    val paths = mutableMapOf<Position, MutableList<HashSet<Position>>>()

    priorityQueue.add(start to 0)
    distances.put(start, 0)
    paths.put(start, mutableListOf(hashSetOf(start)))

    while(!priorityQueue.isEmpty()) {
        val (currPos, currDist) = priorityQueue.poll()

        for (nextPos in neighbours(currPos.pos, map)) {
            val totalDist = currDist + if (currPos.dir == nextPos.dir) 1 else 1001

            if (totalDist < distances.getValue(nextPos)) {
                distances[nextPos] = totalDist
                paths[nextPos] = paths[currPos]!!.map { (it + nextPos).toHashSet() }.toMutableList() // OVERRIDE: Paths to nextPos are all paths to currPos + nextPos
                priorityQueue.add(nextPos to totalDist)
            } else if (totalDist == distances.getValue(nextPos)) {
                paths[nextPos]!!.addAll(paths[currPos]!!.map { (it + nextPos).toHashSet() }.toMutableList()) // ADD: Paths to nextPos are all previous paths to nextPos and all paths to currPos + nextPos
            }

        }
    }

    return distances.toMap() to paths.toMap()

}
