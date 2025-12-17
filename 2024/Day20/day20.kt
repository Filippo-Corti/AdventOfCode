import java.io.File
import kotlin.math.abs 

typealias Position = Pair<Int, Int>

val dirs = listOf(
    0 to 1,
    0 to -1,
    1 to 0,
    -1 to 0
)

fun neighbours(p : Position) : List<Position> {
    return dirs
        .map { Position(p.first + it.first, p.second + it.second) }
        .filter { it.first >= 0 && it.first < rows && it.second >= 0 && it.second < cols}
}

var rows : Int = 0
var cols : Int = 0

fun main() {

    val map = mutableMapOf<Position, Char>()
    var start : Position = 0 to 0
    var end : Position = 0 to 0
    val lines = File("input.txt").readLines()

     lines.forEachIndexed { row, line ->
        line.forEachIndexed{ col, cell ->
            map.put(row to col, cell)
            if (cell == 'S') {
                start = row to col
                map.put(start, '.')
            }
            else if (cell == 'E') {
                end = row to col
                map.put(end, '.')
            } else {
                map.put(row to col, cell)
            }
        }
    } 

    rows = lines.size
    cols = lines[0].length

    println(part1(map, start, end))
    println(part2(map, start, end)) 
}

fun bfs(map : MutableMap<Position, Char>, start : Position, end : Position) : Pair<Map<Position, Int>, HashSet<Position>> {

    val visited = hashSetOf<Position>()
    val walls = hashSetOf<Position>()
    val dists = mutableMapOf<Position, Int>()
    val queue = ArrayDeque<Pair<Position, Int>>()
    queue.add(start to 0)

    while(!queue.isEmpty()) {
        val (currPos, dist) = queue.removeFirst()
        dists.put(currPos, dist)
        visited.add(currPos)

        if (currPos == end) {
            return dists.toMap() to walls
        }

        for (nextPos in neighbours(currPos)) {
            if (visited.contains(nextPos)) continue
            if (map[nextPos]!! == '#') {
                walls.add(nextPos)
                continue
            }
            val nextDist = dist + 1
            queue.add(nextPos to nextDist)
        }
    }

    return dists.toMap() to walls

}

fun part1(map : MutableMap<Position, Char>, start : Position, end : Position) : Int {

    val (distancesToEnd, walls) = bfs(map, end, start) 
    val pathPoints = distancesToEnd.keys
    val totalLength = distancesToEnd[start]!!
    val cheats = mutableMapOf<Int, Int>().withDefault { 0 }

    for (path in pathPoints) {

        val pathToEnd = distancesToEnd[path]!!

        val visited = hashSetOf<Position>()
        val queue = ArrayDeque<Pair<Position, Int>>()
        queue.add(path to 0)

        while(!queue.isEmpty()) {
            val (currPos, currDist) = queue.removeFirst()
            visited.add(currPos)

            if (currDist >= 2) continue

            for (nextPos in neighbours(currPos)) {
                if (visited.contains(nextPos)) continue

                if (map[nextPos]!! == '#') {
                    queue.add(nextPos to currDist + 1)
                } else {
                    val nextToEnd = distancesToEnd[nextPos]!!
                    if (pathToEnd < nextToEnd) continue
                    val gain = pathToEnd - nextToEnd - 1 - currDist
                    if (gain > 0)
                        cheats[gain] = cheats.getValue(gain) + 1
                }
            }

        }
        
    }

    //println(cheats)
    return cheats.filter { (k, v) -> k >= 100}.values.sum()
}

fun manhattan(p1 : Position, p2 : Position) : Int {
    return abs(p1.first - p2.first) + abs(p1.second - p2.second)
}

fun part2(map : MutableMap<Position, Char>, start : Position, end : Position) : Int {

    val (distancesToEnd, walls) = bfs(map, end, start) 
    val pathPoints = distancesToEnd.keys
    val totalLength = distancesToEnd[start]!!
    val cheats = mutableMapOf<Int, Int>().withDefault { 0 }

    for (path in pathPoints) {

        val pathToEnd = distancesToEnd[path]!!

        val visited = hashSetOf<Position>()
        val queue = ArrayDeque<Position>()
        queue.add(path)
        visited.add(path)

        while(!queue.isEmpty()) {
            val currPos = queue.removeFirst()
            val currDist = manhattan(path, currPos)

            if (currDist > 20) continue

            if (map[currPos]!! == '.') {
                val currToEnd = distancesToEnd[currPos]!!
                val gain = pathToEnd - currToEnd - currDist
                if (gain > 0) {
                    cheats[gain] = cheats.getValue(gain) + 1
                }
            }

            for (nextPos in neighbours(currPos)) {
                if (visited.contains(nextPos)) continue
                visited.add(nextPos)
                queue.add(nextPos)
            }

        }
     
    }

    //println(cheats)
    return cheats.filter { (k, v) -> k >= 100}.values.sum()
}

