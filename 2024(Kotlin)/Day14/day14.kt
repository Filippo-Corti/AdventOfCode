import java.io.File
import kotlin.math.round
import kotlin.math.abs 

data class Robot(var pos : Pair<Int, Int>, val vel : Pair<Int, Int>)

val rows : Int = 103
val cols : Int = 101

fun quadrant(pos : Pair<Int, Int>) : Int {
    return when {
        pos.first < rows/2 && pos.second < cols/2 -> 1
        pos.first < rows/2 && pos.second > cols/2 -> 2
        pos.first > rows/2 && pos.second < cols/2 -> 3
        pos.first > rows/2 && pos.second > cols/2 -> 4
        else -> 0
    }
}

fun main() {

    val robots : List<Robot> = File("input.txt").readLines().map {
        val matches = Regex("-?[0-9]+").findAll(it).map { it.value.toInt() }.toList()
        Robot(matches[1] to matches[0], matches[3] to matches[2])
    }

    //println(robots)

    println(part1(robots))
    println(part2(robots))
}

fun part1(robots : List<Robot>) : Int {
    val quadrants : MutableList<Int> = mutableListOf(0, 0, 0, 0, 0)

    for (i in 1..100) {
        robots.forEach {
            it.pos = teleport(it)
        }
    }

    robots.forEach {
        quadrants[quadrant(it.pos)]++
    }

    return quadrants[1] * quadrants[2] * quadrants[3] * quadrants[4]
}

fun teleport(robot : Robot) : Pair<Int, Int> {
    var nextRow = robot.pos.first + robot.vel.first
    var nextCol = robot.pos.second + robot.vel.second

    if (nextRow < 0 || nextRow >= rows) nextRow = (nextRow + rows) % rows
    if (nextCol < 0 || nextCol >= cols) nextCol = (nextCol + cols) % cols

    return nextRow to nextCol
}

fun part2(robots : List<Robot>) : Int {
    for (i in 1..10000) {
        val positions : HashSet<Pair<Int, Int>> = HashSet()
        robots.forEach {
            it.pos = teleport(it)
            positions.add(it.pos)
        }
        if (positions.size == 500) {
            println(positions.size)
            printPositions(positions)
            return i + 100 // Moves from Part 1
        }
    }

    return 0

}

fun printPositions(positions : HashSet<Pair<Int, Int>>) {
    for (i in 0 until rows) {
        for (j in 0 until cols) {
            if (positions.contains(i to j)) {
                print("#")
            } else {
                print(".")
            }
        }
        println()
    }
}