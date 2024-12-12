import java.io.File

val dirs = listOf(
    0 to 1,
    0 to -1,
    1 to 0,
    -1 to 0
)

val dirs2 = listOf(
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
            map.put(row*4 to col*4, cell)
            map.put(row*4 to col*4+1, cell)
            map.put(row*4 to col*4+2, cell)
            map.put(row*4 to col*4+3, cell)
            
            map.put(row*4+1 to col*4, cell)
            map.put(row*4+1 to col*4+1, cell)
            map.put(row*4+1 to col*4+2, cell)
            map.put(row*4+1 to col*4+3, cell)
            
            map.put(row*4+2 to col*4, cell)
            map.put(row*4+2 to col*4+1, cell)
            map.put(row*4+2 to col*4+2, cell)
            map.put(row*4+2 to col*4+3, cell)
            
            map.put(row*4+3 to col*4, cell)
            map.put(row*4+3 to col*4+1, cell)
            map.put(row*4+3 to col*4+2, cell)
            map.put(row*4+3 to col*4+3, cell)
        }
    } 

    rows = lines.size*4
    cols = lines[0].length*4


    //println(part1(map))
    println(part2(map))
}

var visited : HashSet<Pair<Int, Int>> = HashSet()

fun part1(map : MutableMap<Pair<Int, Int>, Char>) : Int {
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
    val perimeter = floodFill(map, cell, filled)
    return perimeter * filled.size/16
}

fun floodFill(map : MutableMap<Pair<Int, Int>, Char>, curr : Pair<Int, Int>, filled : HashSet<Pair<Int, Int>>) : Int {
    filled.add(curr)
    visited.add(curr)
    var perimeter = 0
    for ((dr, dc) in dirs) {
        val next = Pair(curr.first + dr, curr.second + dc)

        if (filled.contains(next)) 
            continue
        else if (next.first < 0 || next.first >= rows || next.second < 0 || next.second >= cols) 
            perimeter++
        else if (map[next] != map[curr]) 
            perimeter++
        else 
            perimeter += floodFill(map, next, filled)
    }
    return perimeter
} 


fun part2(map : MutableMap<Pair<Int, Int>, Char>) : Int {
    visited = HashSet() // Reset visited
    var score = 0
    for (cell in map.keys) {
        if (!visited.contains(cell)) {
            score += findRegion2(map, cell)
        }
    }
    return score
}

fun findRegion2(map : MutableMap<Pair<Int, Int>, Char>, cell : Pair<Int, Int>) : Int {
    val filled : HashSet<Pair<Int, Int>> = HashSet() 
    val perimeter : HashSet<Pair<Int, Int>> = HashSet()
    val maybePerimeter : HashSet<Pair<Int, Int>> = HashSet()
    floodFill2(map, cell, filled, perimeter, maybePerimeter)

    for (cell in maybePerimeter) {
        if (!filled.contains(cell)) {
            perimeter.add(cell)
        }
    }

    return filled.size * countSides(perimeter)/16
}

fun floodFill2(map : MutableMap<Pair<Int, Int>, Char>, curr : Pair<Int, Int>, filled : HashSet<Pair<Int, Int>>, perimeter : HashSet<Pair<Int, Int>>, maybePerimeter : HashSet<Pair<Int, Int>>) {
    filled.add(curr)
    visited.add(curr)
    for ((dr, dc) in dirs2) {
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
            floodFill2(map, next, filled, perimeter, maybePerimeter)
    }
} 

fun countSides(perimeter : HashSet<Pair<Int, Int>>) : Int {
    println("Counting for $perimeter")
    val start = perimeter.toList()[0]
    var currPos = start

    var currDir = dirs[0]
    //Find Curr Dir
    for ((dr, dc) in dirs) {
        var check = Pair(currPos.first + dr, currPos.second + dc)
        if (perimeter.contains(check)) {
            currDir = dr to dc
            break
        }
    }

   // println("Starting from point $currPos with dir $currDir")

    var prevPos = -2 to -2
    var sides = 0

    do {
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
        //println("Now in $currPos with dir $currDir. Sides is $sides")
    } while (currPos != start)

    var next = Pair(currPos.first + currDir.first, currPos.second + currDir.second)
    if (!perimeter.contains(next))
        sides++

    println("Found $sides sides")
    return sides
}