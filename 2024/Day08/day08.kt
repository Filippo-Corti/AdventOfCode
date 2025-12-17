import java.io.File
import kotlin.math.abs 

fun main() {

    val antennas = HashMap<Char, MutableList<Pair<Int, Int>>>()
    
    val lines = File("input.txt").readLines()
    lines.forEachIndexed { row, line ->
        line.forEachIndexed{ col, cell ->
            if (cell != '.') {
                val list = antennas.getOrDefault(cell, mutableListOf<Pair<Int, Int>>())
                list.add(row to col)
                antennas.put(cell, list)
            }
        }
    } 

    val rows = lines.size
    val cols = lines[0].length


    println(part1(antennas, rows, cols))
    println(part2(antennas, rows, cols))
}


fun part1(antennas : HashMap<Char, MutableList<Pair<Int, Int>>>, rows : Int, cols : Int) : Int {

    val antinodes : HashSet<Pair<Int, Int>> = HashSet()
   
    for ((freq, list) in antennas) {
        for (i in 0 until list.size - 1) {
            for (j in i + 1 until list.size) {
                val dr = list[i].first - list[j].first
                val dc = list[i].second - list[j].second

                antinodes.add(Pair(list[i].first + dr, list[i].second + dc))
                antinodes.add(Pair(list[j].first - dr, list[j].second - dc))

            }
        }
    }

    return antinodes.filter{ it.first >= 0 && it.first < rows && it.second >= 0 && it.second < cols }.size
} 

fun part2(antennas : HashMap<Char, MutableList<Pair<Int, Int>>>, rows : Int, cols : Int) : Int {

    val antinodes : HashSet<Pair<Int, Int>> = HashSet()
   
    for ((freq, list) in antennas) {
        for (i in 0 until list.size - 1) {
            for (j in i + 1 until list.size) {
                val dr = list[i].first - list[j].first
                val dc = list[i].second - list[j].second

                var k = 0
                while (list[i].first + dr * k >= 0 && list[i].first + dr * k < rows && list[i].second + dc * k >= 0 && list[i].second + dc * k < cols) {
                    antinodes.add(Pair(list[i].first + dr * k, list[i].second + dc * k))
                    k++
                }
                k = 0
                while (list[j].first - dr * k >= 0 && list[j].first - dr * k < rows && list[j].second - dc * k >= 0 && list[j].second - dc * k < cols) {
                    antinodes.add(Pair(list[j].first - dr * k, list[j].second - dc * k))
                    k++
                }

            }
        }
    }

    return antinodes.size
} 


// kotlinc day08.kt -include-runtime -d day08.jar
// java -jar day08.jar