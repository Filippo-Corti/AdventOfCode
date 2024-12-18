import java.io.File

fun charToDir(c : Char) : Pair<Int, Int> {
    return when (c) {
        '^' -> -1 to 0
        'v' -> 1 to 0
        '>' -> 0 to 1
        '<' -> 0 to -1
        else -> 0 to 0
    }
}

fun printMap(map : MutableMap<Pair<Int, Int>, Char>, robot : Pair<Int, Int>) {
    for (i in 0 until rows) {
        for (j in 0 until cols) {
            if (robot == i to j) {
                print("@")
            } else {
                print("${map[i to j]}")
            }
        }
        println()
    }
    println("----------")
}

var rows : Int = 0
var cols : Int = 0

fun main() {
    val (mapLines, movesLines) = File("input.txt").readLines()
        .let { lines -> lines.takeWhile { it.isNotEmpty() } to lines.dropWhile { it.isNotEmpty() }.drop(1) }

    rows = mapLines.size
    cols = mapLines[0].length

    val map : MutableMap<Pair<Int, Int>, Char> = mutableMapOf()
    var robot : Pair<Int, Int> = 0 to 0

    mapLines.forEachIndexed { row, line ->
        line.forEachIndexed{ col, cell ->
            if (cell == '@') {
                robot = row to col
                map.put(row to col, '.')
            } else {
                map.put(row to col, cell)
            }
        }
    } 

    val instructions = movesLines.joinToString("")

    printMap(map, robot)
    println(part1(map, robot, instructions))
}


fun part1(map : MutableMap<Pair<Int, Int>, Char>, robot : Pair<Int, Int>, instructions : String) : Int {
    var currPos = robot

    instructions.forEach { instr ->

        val (dr, dc) = charToDir(instr)

        var moveUntil = Pair(currPos.first + dr, currPos.second + dc)
        while (map[moveUntil] != '.' && map[moveUntil] != '#') { moveUntil = Pair(moveUntil.first + dr, moveUntil.second + dc) }

        if (map[moveUntil] != '#') {
            var idx = moveUntil
            var nextIdx = Pair(idx.first - dr, idx.second - dc)
            
            while (idx != currPos) {
                map[idx] = map[nextIdx]!!
                idx = nextIdx
                nextIdx = Pair(idx.first - dr, idx.second - dc)
            }

            map[currPos] = '.'
            currPos = Pair(currPos.first + dr, currPos.second + dc)
        }

        //printMap(map, currPos)
    }

    return map
        .filter { (k, v) -> v == 'O' }
        .map { (k, v) -> 100 * k.first + k.second }
        .sum()
}

