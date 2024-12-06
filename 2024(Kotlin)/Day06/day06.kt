import java.io.File
import kotlin.math.abs 

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

    println(part1(obstructions, guard, rows, cols))
    println(part2(obstructions, guard, rows, cols))
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

    val occupied : HashSet<Pair<Int, Int>> = HashSet()  
    val positionsToFind : HashSet<Pair<Int, Int>> = HashSet()
    var dir = -1 to 0
    var curr = guard

    while (true) {

        val next = Pair(curr.first + dir.first, curr.second + dir.second)

        if (next.first < 0 || next.first >= rows || next.second < 0 || next.second >= cols) {
            return positionsToFind.size
        }

        if (obstructions.contains(next)) {
            dir = turnRight(dir)
        } else {
            val newObstructions = HashSet(obstructions)
            newObstructions.add(next)
            if (!occupied.contains(next) && isLooping(newObstructions, Pair(curr, dir), rows, cols))
                positionsToFind.add(next)

            curr = next
        }

        occupied.add(curr)
    }

    return positionsToFind.size
}

fun isLooping(obstructions : HashSet<Pair<Int, Int>>, guard : Pair<Pair<Int, Int>, Pair<Int, Int>>, rows : Int, cols : Int) : Boolean {
    
    val occupied : HashSet<Pair<Pair<Int, Int>, Pair<Int, Int>>> = HashSet()
    var curr = guard.first
    var dir = guard.second

    while (true) {
        
        if (occupied.contains(Pair(curr, dir))) 
            return true

        occupied.add(Pair(curr, dir))

        val next = Pair(curr.first + dir.first, curr.second + dir.second)

        if (next.first < 0 || next.first >= rows || next.second < 0 || next.second >= cols)
            return false

        if (obstructions.contains(next)) {
            dir = turnRight(dir)
        } else {
            curr = next
        }
        
    }

    return false
}

// kotlinc day06.kt -include-runtime -d day06.jar
// java -jar day06.jar