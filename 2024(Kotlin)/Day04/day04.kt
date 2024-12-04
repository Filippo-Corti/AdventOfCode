import java.io.File
import kotlin.math.abs 

val XMAS = listOf('X', 'M', 'A', 'S')

val directions = listOf(
    0 to 1,
    0 to -1,
    1 to 0,
    -1 to 0,
    -1 to -1,
    -1 to 1,
    1 to -1,
    1 to 1
)

fun neighbour(input : List<List<Char>>, i : Int, j : Int, dir : Pair<Int, Int>) : Char? {
    val newI = i + dir.first
    val newJ = j + dir.second

    if (newI < 0 || newI >= input.size || newJ < 0 || newJ >= input[0].size)
        return null

    return input[newI][newJ]
}

fun main() {
    val input : List<List<Char>> = File("input.txt").readLines().map { it.toCharArray().toList() }
    println(part1(input))
    println(part2(input))
}

fun part1(input : List<List<Char>>) : Int {
    var count = 0

    for (i in input.indices) {
        for (j in input[0].indices) {
            if (input[i][j] == 'X') {
                count += findXMAS(input, i, j, 'M', 0 to 0)
            }
        }
    }

    return count
} 

fun findXMAS(input : List<List<Char>>, i : Int, j : Int, lookFor : Char, dir : Pair<Int, Int>) : Int {
    var count = 0
    var dirs = listOf<Pair<Int,Int>>()

    if (lookFor == 'M')
        dirs = directions
    else 
        dirs = listOf(dir)

    for (dir in dirs) {
        val newI = i + dir.first
        val newJ = j + dir.second

        if (newI < 0 || newI >= input.size || newJ < 0 || newJ >= input[0].size)
            continue

        if (input[newI][newJ] != lookFor) 
            continue
            
        if (lookFor == 'S')
            return 1
        
        count += findXMAS(input, newI, newJ, XMAS[XMAS.indexOf(lookFor) + 1], dir)
    }
    return count
}

fun part2(input : List<List<Char>>) : Int {
    var count = 0

    for (i in input.indices) {
        for (j in input[0].indices) {
            if (input[i][j] == 'A') {
                    
                if (findMS(input, i, j, (-1 to -1) to (1 to 1)) && findMS(input, i, j, (-1 to 1) to (1 to -1))) 
                    count++
            }
        }
    }

    return count
}

fun findMS(input : List<List<Char>>, i : Int, j : Int, dirs : Pair<Pair<Int, Int>, Pair<Int, Int>>) : Boolean {
    val neighbour1 = neighbour(input, i, j, dirs.first)
    val neighbour2 = neighbour(input, i, j, dirs.second)

    if (neighbour1 == null || neighbour2 == null) 
        return false

    val pattern = "$neighbour1$neighbour2"

    return (pattern == "MS" || pattern == "SM")
}

// kotlinc day04.kt -include-runtime -d day04.jar
// java -jar day04.jar