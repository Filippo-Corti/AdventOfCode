import java.io.File

val dirs = listOf(
    0 to 1,
    0 to -1,
    1 to 0,
    -1 to 0
)

val allDirs = listOf(
    0 to 1,
    0 to -1,
    1 to 0,
    -1 to 0,
    -1 to -1,
    1 to 1,
    1 to -1,
    -1 to 1
)

var rows : Int = 0
var cols : Int = 0

fun main() {
    
    val map : MutableMap<Pair<Int, Int>, Char> = mutableMapOf()

    val lines = File("input.txt").readLines()
    lines.forEachIndexed { row, line ->
        line.forEachIndexed{ col, cell ->
            //Enlarge Map by a factor of 4
            for (i in 0 until 4) {
                for (j in 0 until 4) {
                    map.put(row*4+i to col*4+j, cell)
                }
            }
        }
    } 

    rows = lines.size*4
    cols = lines[0].length*4

    println(part2(map))
}

val visited : HashSet<Pair<Int, Int>> = HashSet()

fun part2(map : MutableMap<Pair<Int, Int>, Char>) : Int {
    var score = 0
    for (cell in map.keys) {
        if (!visited.contains(cell)) {
            score += findRegion(map, cell)
        }
    }
    return score
}

fun findRegion(map : MutableMap<Pair<Int, Int>, Char>, cell : Pair<Int, Int>) : Int {
    val filled : HashSet<Pair<Int, Int>> = HashSet() 
    val perimeter : HashSet<Pair<Int, Int>> = HashSet()
    val maybePerimeter : HashSet<Pair<Int, Int>> = HashSet()
    floodFill(map, cell, filled, perimeter, maybePerimeter)

    perimeter.addAll(maybePerimeter.filter{ !filled.contains(it) })

    return filled.size * countSides(perimeter) / 16
}

// This Flood Fills works by also counting corners as parts of the Perimeter, unlike the one in Part 1
fun floodFill(map : MutableMap<Pair<Int, Int>, Char>, curr : Pair<Int, Int>, filled : HashSet<Pair<Int, Int>>, perimeter : HashSet<Pair<Int, Int>>, maybePerimeter : HashSet<Pair<Int, Int>>) {
    filled.add(curr)
    visited.add(curr)
    for ((dr, dc) in allDirs) {
        val next = Pair(curr.first + dr, curr.second + dc)

        if (filled.contains(next)) 
            continue
        else if (next.first < 0 || next.first >= rows || next.second < 0 || next.second >= cols) 
            perimeter.add(next)
        else if (map[next] != map[curr]) 
            perimeter.add(next)
        else if (dr * dc != 0)
            maybePerimeter.add(next)
        else
            floodFill(map, next, filled, perimeter, maybePerimeter)
    }
}

fun countSides(perimeter : HashSet<Pair<Int, Int>>) : Int {
    var sides = 0
    val notUsed : HashSet<Pair<Int, Int>> = HashSet(perimeter)
    val start = perimeter.toList()[0]
    var currPos = start
    var prevPos = -2 to -2

    var currDir = dirs[0]
    //Find Curr Dir
    for ((dr, dc) in dirs) {
        var check = Pair(currPos.first + dr, currPos.second + dc)
        if (perimeter.contains(check)) {
            currDir = dr to dc
            break
        }
    }

    do {
        notUsed.remove(currPos)
        var check = Pair(currPos.first + currDir.first, currPos.second + currDir.second)
        if (perimeter.contains(check)) {
            prevPos = currPos
            currPos = check
        } else {
            // Find Next Dir
             for ((dr, dc) in dirs) {
                var check = Pair(currPos.first + dr, currPos.second + dc)
                if (check != prevPos && perimeter.contains(check)) {
                    currDir = dr to dc
                    break
                }
            }
            sides++
            prevPos = currPos
            currPos = Pair(currPos.first + currDir.first, currPos.second + currDir.second)
        }
    } while (currPos != start)

    var next = Pair(currPos.first + currDir.first, currPos.second + currDir.second)
    if (!perimeter.contains(next))
        sides++
 
    if (notUsed.size != 0)
        sides += countSides(notUsed)

    return sides
}