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
}


fun part1(map : Map<Pair<Int, Int>, Char>) : Int {
    var start = rows-2 to 1 // Starting Position
    var startDir = 0 to 1 // Starting Direction (Est)
 
    val distances = mutableMapOf<Position, Int>().withDefault { Int.MAX_VALUE }
    val priorityQueue = PriorityQueue<Pair<Position, Int>>( compareBy { it.second })
    priorityQueue.add(Position(start, startDir) to 0)
    distances.put(Position(start, startDir), 0)

    while(!priorityQueue.isEmpty()) {
        val (currPos, currDist) = priorityQueue.poll()
        // println("Popped $currPos, $currDist")
        if (map[currPos.pos]!! == 'E') return currDist

        for (nextPos in neighbours(currPos.pos, map)) {
            val totalDist = currDist + if (currPos.dir == nextPos.dir) 1 else 1001
            if (totalDist < distances.getValue(nextPos)) {
                distances[nextPos] = totalDist
                priorityQueue.add(nextPos to totalDist)
            }
        }
    }

    return 0
}
