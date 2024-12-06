import java.io.File
import kotlin.math.abs 

val dirs = listOf(
    0 to 1,
    0 to -1,
    1 to 0,
    -1 to 0
)

fun turnRight(dir : Pair<Int, Int>) : Pair<Int, Int> {
    return when (dir) {
        0 to 1 -> 1 to 0
        0 to -1 -> -1 to 0
        1 to 0 -> 0 to -1
        -1 to 0 -> 0 to 1
        else -> 0 to 0
    }
}

fun main() {

    val obstructions = HashSet<Pair<Int, Int>>()
    var guard = 0 to 0
    
    val lines = File("input.txt").readLines()
    lines.forEachIndexed { row, line ->
        line.forEachIndexed{ col, cell ->
            if (cell == '#') {
                obstructions.add(Pair(row, col))
            } else if (cell == '^') {
                guard = row to col
            }
        }
    } 

    val rows = lines.size
    val cols = lines[0].length


    //println(guard)
    //println(obstructions)

    println(part1(obstructions, guard, rows, cols))
    // println(part2(rules, updates))
    //println(part2(text))
}


fun part1(obstructions : HashSet<Pair<Int, Int>>, guard : Pair<Int, Int>, rows : Int, cols : Int) : Int {
    
    val occupied : HashSet<Pair<Int, Int>> = HashSet()
    var dir = -1 to 0
    var curr = guard

    occupied.add(curr)
    while (true) {
        val next = Pair(curr.first + dir.first, curr.second + dir.second)

        //println("Next is $next")

        if (next.first < 0 || next.first >= rows || next.second < 0 || next.second >= cols)
            return occupied.size


        if (obstructions.contains(next)) {
            dir = turnRight(dir)
        } else {
            occupied.add(next)
            curr = next
        }
    }

    return occupied.size
} 

fun isValidUpdate(order : List<Int>, update : List<Int>) : Boolean {
    var lastIndex = order.indexOf(update[0])
    for (i in 1 until update.size) {
        val currentIndex = order.indexOf(update[i])
        if (currentIndex <= lastIndex) 
            return false
        lastIndex = currentIndex
    }
    return true
}

fun part2(rules : List<String>, updates : List<String>) : Int {
    return 0
}
// kotlinc day06.kt -include-runtime -d day06.jar
// java -jar day06.jar