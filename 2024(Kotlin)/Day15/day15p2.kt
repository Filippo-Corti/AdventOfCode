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
    cols = mapLines[0].length * 2

    val map : MutableMap<Pair<Int, Int>, Char> = mutableMapOf()
    var robot : Pair<Int, Int> = 0 to 0

    mapLines.forEachIndexed { row, line ->
        line.forEachIndexed{ col, cell ->
            when (cell) {
                '@' -> {
                    robot = row to col*2
                    map.put(row to col*2, '.')
                    map.put(row to col*2+1, '.')
                }
                'O' -> {
                    map.put(row to col*2, '[')
                    map.put(row to col*2+1, ']')
                }
                '.' -> {
                    map.put(row to col*2, '.')
                    map.put(row to col*2+1, '.')
                }
                '#' -> {
                    map.put(row to col*2, '#')
                    map.put(row to col*2+1, '#')
                }
            }
        }
    } 

    val instructions = movesLines.joinToString("")

    printMap(map, robot)
    println(part2(map, robot, instructions))
}


fun part2(map : MutableMap<Pair<Int, Int>, Char>, robot : Pair<Int, Int>, instructions : String) : Int {
    var currPos = robot

    instructions.forEach { instr ->

        val (dr, dc) = charToDir(instr)

        var moveUntil = Pair(currPos.first + dr, currPos.second + dc)

        var toMove : MutableMap<Pair<Int, Int>, Char> = mutableMapOf() 
        var queue = ArrayDeque<Pair<Int, Int>>()
        queue.add(moveUntil)
        while (!queue.isEmpty()) { 
            val pos = queue.removeFirst()
            val v = map[pos]!!

            //println("Queue is $queue, popped $pos $v")

            if (v == '#') {
                toMove = mutableMapOf()
                break
            }

            if (v == '.') {
                toMove.put(pos, v)
                continue
            }

            if (v == '[' || v == ']') {
                toMove.put(pos, v)

                val next = Pair(pos.first + dr, pos.second + dc)
                if (!toMove.containsKey(next) && !queue.contains(next)) {
                    queue.add(next)
                }

                if (dc == 0) { // If moving Vertical
                    val pair = Pair(pos.first, pos.second + (if (v == '[') 1 else -1))
                    if (!toMove.containsKey(pair) && !queue.contains(pair)) {
                        queue.add(pair)
                    }
                }


            }
        }

        if (!toMove.isEmpty()) {

            //println("We are moving $toMove")

            for((pos, v) in toMove) {
                val newPos = Pair(pos.first - dr, pos.second - dc)     
                map[pos] = if (toMove.containsKey(newPos)) toMove[newPos]!! else '.'
            }

            map[currPos] = '.'
            currPos = Pair(currPos.first + dr, currPos.second + dc)
        }

    }
    
    printMap(map, currPos)

    return map
        .filter { (k, v) -> v == '[' }
        .map { (k, v) -> 100 * k.first + k.second }
        .sum()
}

