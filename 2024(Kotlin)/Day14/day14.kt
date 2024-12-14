import java.io.File
import kotlin.math.round
import kotlin.math.abs 

data class Robot(val pos : Pair<Int, Int>, val vel : Pair<Int, Int>)

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

    println(robots)

    println(part1(robots))
}

fun part1(robots : List<Robot>) : Int {
    val quadrants : MutableList<Int> = mutableListOf(0, 0, 0, 0, 0)
    robots.forEach {
        val p = teleport(it, 100)
        println("Final position is $p, quadrant is ${quadrant(p)}")
        quadrants[quadrant(p)]++
    }
    println(quadrants)
    return quadrants[1] * quadrants[2] * quadrants[3] * quadrants[4]
}

fun teleport(robot : Robot, seconds : Int) : Pair<Int, Int> {
    val visited : MutableMap<Pair<Int, Int>, Int> = mutableMapOf()

    var curr = robot.pos
    val (dr, dc) = robot.vel

    visited.put(curr, 0)

    for (i in 1..seconds) {
       // println("Currently in $curr")
        var nextRow = curr.first + dr
        var nextCol = curr.second + dc

        if (nextRow < 0 || nextRow >= rows) nextRow = (nextRow + rows) % rows
        if (nextCol < 0 || nextCol >= cols) nextCol = (nextCol + cols) % cols

        curr = nextRow to nextCol
        visited.put(curr, i+1)

    }

    return curr

}
