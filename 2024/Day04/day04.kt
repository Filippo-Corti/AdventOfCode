import java.io.File
import kotlin.math.abs 

val XMAS = listOf('X', 'M', 'A', 'S')

fun nextChar(c : Char) : Char? {
    val index = XMAS.indexOf(c)
    if (index == -1 || index == XMAS.size - 1) return null
    return XMAS[index + 1]
}

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
                for (dir in directions) {
                    
                    val newI = i + dir.first
                    val newJ = j + dir.second

                    if (newI < 0 || newI >= input.size || newJ < 0 || newJ >= input[0].size)
                        continue
                    
                    if (input[newI][newJ] == 'M') {
                        count += findXMASDirected(input, newI, newJ, 'A', dir)
                    }
                }
            }
        }
    }

    return count
} 

fun part2(input : List<List<Char>>) : Int {
    var count = 0

    for (i in input.indices) {
        for (j in input[0].indices) {
            if (input[i][j] == 'A') {
                    
                //Diagonal 1
                val neighbour1 = neighbour(input, i, j, -1 to -1)
                val neighbour2 = neighbour(input, i, j, 1 to 1)

                if (neighbour1 == null || neighbour2 == null) 
                    continue

                val pattern = "$neighbour1$neighbour2"

                if (pattern != "MS" && pattern != "SM")
                    continue
                
                //Diagonal 2
                val neighbour3 = neighbour(input, i, j, -1 to 1)
                val neighbour4 = neighbour(input, i, j, 1 to -1)

                if (neighbour3 == null || neighbour4 == null) 
                    continue

                val pattern2 = "$neighbour3$neighbour4"

                if (pattern2 == "MS" || pattern2 == "SM")
                    count++
            }
        }
    }

    return count
}

fun findXMASDirected(input : List<List<Char>>, i : Int, j : Int, lookFor : Char, dir : Pair<Int, Int>) : Int {
    var count = 0
    val nextLookFor = nextChar(lookFor)

    val newI = i + dir.first
    val newJ = j + dir.second

    if (newI < 0 || newI >= input.size || newJ < 0 || newJ >= input[0].size)
        return 0

    if (input[i + dir.first][j + dir.second] == lookFor) {
        if (nextLookFor != null)
            count += findXMASDirected(input, i + dir.first, j + dir.second, nextLookFor, dir)
        else
            count++
    }
    return count
}

// kotlinc day04.kt -include-runtime -d day04.jar
// java -jar day04.jar