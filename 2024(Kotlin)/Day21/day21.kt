import java.io.File
import kotlin.math.abs

typealias Position = Pair<Int, Int>

val dirs = listOf(
    0 to 1,
    0 to -1,
    1 to 0,
    -1 to 0
)

val dirChars = mapOf(
    (0 to 1) to ">",
    (0 to -1) to "<",
    (1 to 0) to "v",
    (-1 to 0) to "^"
)

fun manhattan(p1 : Position, p2 : Position) : Int {
    return abs(p1.first - p2.first) + abs(p1.second - p2.second)
}

object NumPad {

    val states = mapOf<Char, Position>(
        '0' to Position(3, 1), 
        '1' to Position(2, 0), 
        '2' to Position(2, 1), 
        '3' to Position(2, 2), 
        '4' to Position(1, 0), 
        '5' to Position(1, 1), 
        '6' to Position(1, 2), 
        '7' to Position(0, 0), 
        '8' to Position(0, 1), 
        '9' to Position(0, 2), 
        'A' to Position(3, 2)
    ).withDefault { Position(3, 2) }

    val positions = states.entries.associate { (key, value) -> value to key }

    
    val memory = mutableMapOf<Pair<Char, Char>, List<String>>()
    
    fun allBestPathsBetween(curr : Char, target : Char) : List<String> {
        if (curr == target) return mutableListOf<String>("A")
        
        if(memory.containsKey(curr to target)) {
            return memory[curr to target]!!
        }

        val currPos = states.getValue(curr)
        val targetPos = states.getValue(target)
        val allPathsFromHere = mutableListOf<String>()

        for (dir in dirs) {
            val nextPos = Position(currPos.first + dir.first, currPos.second + dir.second)

            if (!positions.containsKey(nextPos)) continue  // Only if I'm still on the pad
            if (manhattan(nextPos, targetPos) >= manhattan(currPos, targetPos)) continue // Only if I'm getting closer

            val next = positions.getValue(nextPos)
            val dirString = dirChars[dir]!!
            val allPathsFromNext = allBestPathsBetween(next, target)
            allPathsFromHere.addAll(allPathsFromNext.map { dirString + it })
        }

        memory[curr to target] = allPathsFromHere
        return allPathsFromHere.toList()
    }

    fun allBestPaths(str : String) : List<String> {
        var i = 0
        var allPaths = mutableListOf<String>("")
        while(i < str.length) {
            val newPaths = allBestPathsBetween(if (i == 0) 'A' else str[i-1], str[i])
            val allNewPaths = mutableListOf<String>()
            for(p1 in allPaths) {
                for (p2 in newPaths) {
                    allNewPaths.add("$p1$p2")
                }
            }
            allPaths = allNewPaths
            i++
        }
        return allPaths.toList()
    }

}

object DirPad {

    val states = mapOf<Char, Position>(
        '^' to Position(0, 1), 
        'A' to Position(0, 2),
        '<' to Position(1, 0), 
        'v' to Position(1, 1), 
        '>' to Position(1, 2)
    ).withDefault { Position(0, 2) }

    val positions = states.entries.associate { (key, value) -> value to key }

    val memory = mutableMapOf<Pair<Char, Char>, List<String>>()
    
    fun allBestPathsBetween(curr : Char, target : Char) : List<String> {
        if (curr == target) return mutableListOf<String>("A")
        
        if(memory.containsKey(curr to target)) {
            return memory[curr to target]!!
        }

        val currPos = states.getValue(curr)
        val targetPos = states.getValue(target)
        val allPathsFromHere = mutableListOf<String>()

        for (dir in dirs) {
            val nextPos = Position(currPos.first + dir.first, currPos.second + dir.second)

            if (!positions.containsKey(nextPos)) continue  // Only if I'm still on the pad
            if (manhattan(nextPos, targetPos) >= manhattan(currPos, targetPos)) continue // Only if I'm getting closer

            val next = positions.getValue(nextPos)
            val dirString = dirChars[dir]!!
            val allPathsFromNext = allBestPathsBetween(next, target)
            allPathsFromHere.addAll(allPathsFromNext.map { dirString + it })
        }

        memory[curr to target] = allPathsFromHere
        return allPathsFromHere.toList()
    }

    fun allBestPaths(str : String) : List<String> {
        var i = 0
        var allPaths = mutableListOf<String>("")
        while(i < str.length) {
            val newPaths = allBestPathsBetween(if (i == 0) 'A' else str[i-1], str[i])
            val allNewPaths = mutableListOf<String>()
            for(p1 in allPaths) {
                for (p2 in newPaths) {
                    allNewPaths.add("$p1$p2")
                }
            }
            allPaths = allNewPaths
            i++
        }
        return allPaths.toList()
    }
}


fun main() {

    val lines = File("input.txt").readLines()

    println(part1(lines))
    println(part2(lines))
}

fun part1(sequences : List<String>) : Int {
    var sum = 0

    for(seq in sequences) {
        var paths = NumPad.allBestPaths(seq)
        for (i in 0 until 2) {
            var newPaths = paths.map { DirPad.allBestPaths(it) }.flatMap { it }
            val minLen = newPaths.minOf { it.length }
            paths = newPaths.filter { it.length == minLen }
        }

        val res = paths.minBy { it.length }
        sum += res.length * seq.substring(0, 3).toInt()
    }
    return sum
}

fun part2(sequences : List<String>) : Int {
    var sum = 0

    for(seq in sequences) {
        var paths = NumPad.allBestPaths(seq)
        for (i in 0 until 25) {
            var newPaths = paths.map { DirPad.allBestPaths(it) }.flatMap { it }
            val minLen = newPaths.minOf { it.length }
            paths = newPaths.filter { it.length == minLen }
            println("$i, ${paths.size}")
        }

        val res = paths.minBy { it.length }
        sum += res.length * seq.substring(0, 3).toInt()
    }
    return sum
}

// v<A<AA>>^AvAA<^A>Av<<A>>^AvA^Av<<A>>^AAvA<A>^A<A>Av<A<A>>^AAAvA<^A>A
// <vA<AA>>^AvAA<^A>A<v<A>>^AvA^A<vA>^A<v<A>^A>AAvA^A<v<A>A>^AAAvA<^A>A

// v<<A>>^AAAvA^Av<A<AA>>^AvAA<^A>Av<A<A>>^AAAvA<^A>Av<A>^A<A>A
// <v<A>>^AAAvA^A<vA<AA>>^AvAA<^A>A<v<A>A>^AAAvA<^A>A<vA>^A<A>A

// v<<A>>^Av<A<A>>^AAvAA<^A>Av<<A>>^AAvA^Av<A>^AA<A>Av<A<A>>^AAAvA<^A>A
// <v<A>>^A<vA<A>>^AAvAA<^A>A<v<A>>^AAvA^A<vA>^AA<A>A<v<A>A>^AAAvA<^A>A

// v<<A>>^AAv<A<A>>^AAvAA<^A>Av<A>^A<A>Av<A>^A<A>Av<A<A>>^AAvA<^A>A
// <v<A>>^AA<vA<A>>^AAvAA<^A>A<vA>^A<A>A<vA>^A<A>A<v<A>A>^AAvA<^A>A

// v<<A>>^AvA^Av<<A>>^AAv<A<A>>^AAvAA<^A>Av<A>^AA<A>Av<A<A>>^AAAvA<^A>A
// <v<A>>^AvA^A<vA<AA>>^AAvA<^A>AAvA^A<vA>^AA<A>A<v<A>A>^AAAvA<^A>A