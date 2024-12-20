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
    val cheats = mutableMapOf<Int, HashSet<Pair<Position, Position>>>().withDefault { hashSetOf() }

    for (wall in walls) {
        var pathsNextToWall = hashSetOf<Position>()
        val wallsNextToWall = hashSetOf<Position>()
        for (neighbour in neighbours(wall)) {
            if (map[neighbour]!! == '.' ) {
                pathsNextToWall.add(neighbour)
            } else if (walls.contains(neighbour)) {
                wallsNextToWall.add(neighbour)
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
        // println("The wall at $wall has:")
        // println("\tPaths next to it: $pathsNextToWall")
        // println("\tWalls next to it: $wallsNextToWall")
        // println("\t THE WALL HAS A SHORCUT with a total length of $pathLength and gain $gain")
            cheats[gain] = cheats[gain]?.apply { add(furthestFromEnd to closestToEnd) } ?: hashSetOf(furthestFromEnd to closestToEnd)
        }

        } else  {
            for (nextToWall in wallsNextToWall) {
                val gains = neighbours(nextToWall)
                    .filter { map[it]!! == '.'}
                    .map {
                        val neighbourToEndVal = distancesToEnd[it]!!
                        
                        val pathLength = totalLength - furthestFromEndVal + neighbourToEndVal
                        val gain = totalLength - pathLength - 1
                        it to gain
                    }
                    .filter { it.second > 0}
                
                if (gains.size > 0) {
                    val gain = gains.maxBy { it.second }
                    println("The wall at $wall has:")
                    println("\tPaths next to it: $pathsNextToWall")
                    println("\tWalls next to it: $wallsNextToWall")
                    println("\t THE WALL HAS A DOUBLE SHORCUT with a total gain of $gain")
                    //cheats[gain] = cheats.getValue(gain) + 1
                    cheats[gain.second] = cheats[gain.second]?.apply { add(furthestFromEnd to gain.first) } ?: hashSetOf(furthestFromEnd to gain.first)
                }
                // for (neighbour in neighbours(nextToWall)) {
                //     if (map[neighbour]!! == '.') {
                //         val neighbourToEnd = distancesToEnd[neighbour]!!
                        
                //         val pathLength = totalLength - furthestFromEnd + neighbourToEnd
                //         val gain = totalLength - pathLength - 1

                //         if (gain > 0) {
                //             println("The wall at $wall has:")
                //             println("\tPaths next to it: $pathsNextToWall")
                //             println("\tWalls next to it: $wallsNextToWall")
                //             println("\t THE WALL HAS A DOUBLE SHORCUT with a total length of $pathLength and gain $gain")
                //             cheats[gain] = cheats.getValue(gain) + 1
                //         }
                //     }
                // }
            }
        }


        //   if (pathLength > 0) {
        //     println("The wall at $wall has:")
        //     println("\tPaths next to it: $pathsNextToWall")
        //     println("\tWalls next to it: $wallsNextToWall")
        //     println("\t THE WALL HAS A SHORCUT with a total length of $pathLength")

        //    }
        

    }

    for ((k, v) in cheats) {
        println("There ar ${v.size} cheats that save $k picoseconds")
    }

    return 0
}