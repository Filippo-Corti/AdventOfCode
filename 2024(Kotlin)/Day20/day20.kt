import java.io.File

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
        .filter { it.first > 0 && it.first < rows - 1 && it.second > 0 && it.second < cols - 1}
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
    val totalLength = distancesToEnd[start]!!
    val cheats = mutableMapOf<Int, Int>().withDefault { 0 }

    for (wall in walls) {
        var pathsNextToWall = neighbours(wall).filter { map[it]!! == '.'}
        
        if (pathsNextToWall.size < 1) continue 

        val comingFrom = pathsNextToWall.maxOf { distancesToEnd[it]!! }
        val goingTo = pathsNextToWall.minOf { distancesToEnd[it]!! }
        val gain = comingFrom - goingTo - 2

        cheats[gain] = cheats.getValue(gain) + 1
    }

    return cheats.filter { (k, v) -> k >= 100}.values.sum()
}

fun part2(map : MutableMap<Position, Char>, start : Position, end : Position) : Int {

    val (distancesToEnd, walls) = bfs(map, end, start) 
    val totalLength = distancesToEnd[start]!!
    val cheats = mutableMapOf<Int, Int>().withDefault { 0 }

    for (wall in walls) {
        var pathsNextToWall = neighbours(wall).filter { map[it]!! == '.'}
        
        if (pathsNextToWall.size < 1) continue 

        val comingFrom = pathsNextToWall.maxOf { distancesToEnd[it]!! }
        val closestToEnd = pathsNextToWall.minOf { distancesToEnd[it]!! }
        val gain = furthestFromEnd - closestToEnd - 2

        cheats[gain] = cheats.getValue(gain) + 1
    }

    return cheats.filter { (k, v) -> k >= 100}.values.sum()
}

/*
fun part2(map : MutableMap<Position, Char>, start : Position, end : Position) : Int {

    val (distancesToEnd, walls) = bfs(map, end, start) 
    val totalLength = distancesToEnd[start]!!
    val cheats = mutableMapOf<Int, HashSet<Pair<Position, Position>>>()

    for (wall in walls) {
        var pathsNextToWall = hashSetOf<Position>()
        for (neighbour in neighbours(wall)) {
            if (map[neighbour]!! == '.' ) {
                pathsNextToWall.add(neighbour)
            }
        }
        
        val furthestFromEnd = pathsNextToWall.maxBy { distancesToEnd[it]!! }
        val furthestFromEndVal = distancesToEnd[furthestFromEnd]!!

        if (pathsNextToWall.size > 1) {
            val closestToEnd = pathsNextToWall.minBy { distancesToEnd[it]!! }
            val closestToEndVal = distancesToEnd[closestToEnd]!!
            val pathLength = totalLength - furthestFromEndVal + closestToEndVal
            val gain = totalLength - pathLength - 2

            if (gain > 0) {
                cheats[gain] = cheats.getValue(gain) + 1
            }
        }
    }

    println(cheats)
    return cheats.filter { (k, v) -> k >= 100}.values.sum()
}*/