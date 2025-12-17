import java.io.File
import java.util.PriorityQueue

val dirs = listOf(
    0 to 1,
    0 to -1,
    1 to 0,
    -1 to 0
)

val rows : Int = 71
val cols : Int = 71

fun neighbours(pos : Pair<Int, Int>, bytes : HashSet<Pair<Int, Int>>) : List<Pair<Int, Int>> {
    return dirs
        .map { Pair(pos.first + it.first, pos.second + it.second) }
        .filter { !bytes.contains(it) }
        .filter { it.first >= 0 && it.first < rows && it.second >= 0 && it.second < cols }
}

fun main() {

    val bytes = File("input.txt").readLines().map { 
        val x = it.split(",")
        Pair(x[0].toInt(), x[1].toInt())
    }


    println(part1(bytes.subList(0, 1024).toHashSet()))
    println(part2(bytes))
}

fun dijkstra(start : Pair<Int, Int>, end : Pair<Int, Int>, bytes : HashSet<Pair<Int, Int>>) : Int {
    val distances = mutableMapOf<Pair<Int, Int>, Int>().withDefault { Int.MAX_VALUE }
    val priorityQueue = PriorityQueue<Pair<Pair<Int, Int>, Int>>( compareBy { it.second }) 

    distances.put(start, 0)
    priorityQueue.add(start to 0)

    while (!priorityQueue.isEmpty()) {
        val (currPos, currDist) = priorityQueue.poll()

        if (currPos == end) {
            return currDist
        }

        for (next in neighbours(currPos, bytes)) {
            val totalDist = currDist + 1
            if (totalDist < distances.getValue(next)) {
                distances[next] = totalDist
                priorityQueue.add(next to totalDist)
            }
        }
    }

    return distances.getValue(end)
}

fun part1(bytes : HashSet<Pair<Int, Int>>) : Int {
    return dijkstra(0 to 0, rows-1 to rows-1, bytes)
}

fun part2(bytes : List<Pair<Int, Int>>) : Pair<Int, Int> {
    for (i in 0 until bytes.size) {
        val currBytes = bytes.subList(0, bytes.size - i)
        val dist = dijkstra(0 to 0, rows-1 to rows-1, currBytes.toHashSet())
        if (dist < Int.MAX_VALUE) {
            return bytes[bytes.size - i]
        }
    }
    return 0 to 0
}