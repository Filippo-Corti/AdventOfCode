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

val numStates = mapOf<Char, Position>(
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
)

val dirStates = mapOf<Char, Position>(
    '^' to Position(0, 1), 
    'A' to Position(0, 2),
    '<' to Position(1, 0), 
    'v' to Position(1, 1), 
    '>' to Position(1, 2)
)

fun manhattan(p1 : Position, p2 : Position) : Int {
    return abs(p1.first - p2.first) + abs(p1.second - p2.second)
}

open class Pad (
    val states : Map<Char, Position>
) {

    val positions = states.entries.associate { (key, value) -> value to key }

    val pathsMemory = mutableMapOf<Pair<Char, Char>, List<String>>()

    open fun allBestPathsBetween(curr : Char, target : Char) : List<String> {
        if (curr == target) return mutableListOf<String>("A")
        
        if(pathsMemory.containsKey(curr to target)) {
            return pathsMemory[curr to target]!!
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

        pathsMemory[curr to target] = allPathsFromHere
        return allPathsFromHere.toList()
    }

}

class NumPad (
    dirPadsCount : Int = 2
) : Pad(numStates) {

    val dirPads = DirPad(dirPadsCount)

    val complexitiesMemory = mutableMapOf<Pair<Char, Char>, Long>()
    
    fun calcComplexity(curr : Char, target : Char) : Long {
        if (curr == target) return 1 // "A"
        
        if(complexitiesMemory.containsKey(curr to target)) {
            return complexitiesMemory[curr to target]!!
        }

        val paths = super.allBestPathsBetween(curr, target)
        val result = paths.map { dirPads.calcTotalComplexity(it) }.min()

        complexitiesMemory[curr to target] = result
        return result
    }

    fun calcTotalComplexity(str : String) : Long {
        val withPrefix = "A$str"
        return withPrefix.zipWithNext { curr, next -> calcComplexity(curr, next)}.sum()
    }

}

class DirPad(
    val padsCount : Int = 2
) : Pad(dirStates) {

    val complexitiesMemory = mutableMapOf<Triple<Int, Char, Char>, Long>()

    fun calcComplexity(curr : Char, target : Char, padNumber : Int = 1) : Long {
        if (curr == target) return 1 // "A"
        
        val key = Triple(padNumber, curr, target)

        if(complexitiesMemory.containsKey(key)) {
            return complexitiesMemory[key]!!
        }

        val paths = super.allBestPathsBetween(curr, target)
        var result = 0L
        if (padNumber < padsCount) {
            result = paths.map { calcTotalComplexity(it, padNumber + 1) }.min()
        } else {
            result = paths.minOf { it.length }.toLong()
        }

        complexitiesMemory[key] = result
        return result
    }

    fun calcTotalComplexity(str : String, padNumber : Int = 1) : Long {
        val withPrefix = "A$str"
        return withPrefix.zipWithNext { curr, next -> calcComplexity(curr, next, padNumber)}.sum()
    }
}

fun main() {

    val lines = File("input.txt").readLines()

    println(part1(lines))
    println(part2(lines))
}

fun part1(sequences : List<String>) : Long {
    var sum = 0L
    val numPad = NumPad(
        dirPadsCount = 2
    )
    for(seq in sequences) {
        val res = numPad.calcTotalComplexity(seq)
        sum += res * seq.substring(0, 3).toInt()
    }
    return sum
}

fun part2(sequences : List<String>) : Long {
    var sum = 0L
    val numPad = NumPad(
        dirPadsCount = 25
    )
    for(seq in sequences) {
        val res = numPad.calcTotalComplexity(seq)
        sum += res * seq.substring(0, 3).toInt()
    }
    return sum
}