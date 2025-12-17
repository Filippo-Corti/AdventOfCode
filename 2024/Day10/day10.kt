import java.io.File

val dirs = listOf(
    0 to 1,
    0 to -1,
    1 to 0,
    -1 to 0
)

fun neighbours(pos : Pair<Int, Int>, rows : Int, cols : Int) : List<Pair<Int, Int>> {
    return dirs
        .map { Pair(pos.first + it.first, pos.second + it.second) }
        .filter { it.first >= 0 && it.first < rows && it.second >= 0 && it.second < cols}
}

fun main() {
    
    val map = HashMap<Pair<Int, Int>, Int>()
    val trailheads = HashSet<Pair<Int, Int>>()

    val lines = File("input.txt").readLines()
    lines.forEachIndexed { row, line ->
        line.forEachIndexed{ col, cell ->
            val intVal = cell.digitToInt()
            map.put(row to col, intVal)
            if (intVal == 0)
                trailheads.add(row to col)
        }
    } 

    val rows = lines.size
    val cols = lines[0].length


    println(part1(map, trailheads, rows, cols))
    println(part2(map, trailheads, rows, cols))

}

fun trailheadScore(trailhead : Pair<Int, Int>, map : HashMap<Pair<Int, Int>, Int>, rows : Int, cols : Int) : Int {
    
    val nines = HashSet<Pair<Int, Int>>()

    val queue = ArrayDeque<Pair<Int, Int>>()
    queue.add(trailhead)

    while(!queue.isEmpty()) {
        val currPos = queue.removeFirst()
        val currVal = map[currPos]!!

        if (currVal == 9) {
            nines.add(currPos)
            continue
        }

        for (nextPos in neighbours(currPos, rows, cols)) {
            val nextVal = map[nextPos]!!
            if (nextVal == currVal + 1) {
                queue.add(nextPos)
            }
        }
    }
    return nines.size
}

fun part1(map : HashMap<Pair<Int, Int>, Int>, trailheads : HashSet<Pair<Int, Int>>, rows : Int, cols : Int) : Int {
    return trailheads.map{ trailheadScore(it, map, rows, cols) }.sum()
}

fun trailheadRating(trailhead : Pair<Int, Int>, map : HashMap<Pair<Int, Int>, Int>, rows : Int, cols : Int) : Int {
    
    var count = 0

    val queue = ArrayDeque<Pair<Int, Int>>()
    queue.add(trailhead)

    while(!queue.isEmpty()) {
        val currPos = queue.removeFirst()
        val currVal = map[currPos]!!

        if (currVal == 9) {
            count++
            continue
        }

        for (nextPos in neighbours(currPos, rows, cols)) {
            val nextVal = map[nextPos]!!
            if (nextVal == currVal + 1) {
                queue.add(nextPos)
            }
        }
    }
    
    return count
}


fun part2(map : HashMap<Pair<Int, Int>, Int>, trailheads : HashSet<Pair<Int, Int>>, rows : Int, cols : Int) : Int {
    return trailheads.map{ trailheadRating(it, map, rows, cols) }.sum()
}

// kotlinc day10.kt -include-runtime -d day10.jar
// java -jar day10.jar