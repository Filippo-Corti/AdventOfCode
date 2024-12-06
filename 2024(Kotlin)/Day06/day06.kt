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



    //println(part1(obstructions, guard, rows, cols))
    println(part2(obstructions, guard, rows, cols)) //1767
}


fun part1(obstructions : HashSet<Pair<Int, Int>>, guard : Pair<Int, Int>, rows : Int, cols : Int) : Int {
    
    val occupied : HashSet<Pair<Int, Int>> = HashSet()
    var dir = -1 to 0
    var curr = guard

    occupied.add(curr)
    while (true) {
        val next = Pair(curr.first + dir.first, curr.second + dir.second)

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

fun part2(obstructions : HashSet<Pair<Int, Int>>, guard : Pair<Int, Int>, rows : Int, cols : Int) : Int  {
    
    val occupied : HashSet<Pair<Pair<Int, Int>, Pair<Int, Int>>> = HashSet()
    val positionsToFind : HashSet<Pair<Int, Int>> = HashSet()
    var dir = -1 to 0
    var curr = guard

    var i = 0
    while (true) {
        val next = Pair(curr.first + dir.first, curr.second + dir.second)

        if (next.first < 0 || next.first >= rows || next.second < 0 || next.second >= cols) {
            //println(positionsToFind)
            return positionsToFind.size
        }

        //println("$i")
        i++

        if (obstructions.contains(next)) {
            dir = turnRight(dir)
        } else {
            val newObstructions = obstructions.toMutableSet()
            newObstructions.add(next)
            if (isLooping(newObstructions.toHashSet(), Pair(curr, turnRight(dir)), rows, cols))
                positionsToFind.add(next)

            curr = next
        }

    }

    return positionsToFind.size
}

fun isLooping(obstructions : HashSet<Pair<Int, Int>>, guard : Pair<Pair<Int, Int>, Pair<Int, Int>>, rows : Int, cols : Int) : Boolean {
    
    val occupied : HashSet<Pair<Pair<Int, Int>, Pair<Int, Int>>> = HashSet()
    var curr = guard.first
    var dir = guard.second

    occupied.add(guard)
    while (true) {
        val next = Pair(curr.first + dir.first, curr.second + dir.second)

        //println("Next is $next $dir")

        if (next.first < 0 || next.first >= rows || next.second < 0 || next.second >= cols)
            return false

        if (obstructions.contains(next)) {
            dir = turnRight(dir)
        } else {
            if (occupied.contains(Pair(next, dir))) 
                return true

            curr = next

            occupied.add(Pair(curr, dir))
        }
        
    }

    return false
}

// kotlinc day06.kt -include-runtime -d day06.jar
// java -jar day06.jar