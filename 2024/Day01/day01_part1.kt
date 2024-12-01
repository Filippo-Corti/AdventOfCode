import java.io.File
import kotlin.math.abs 

fun main() {
    val lines: List<String> = File("input.txt").readLines()

    println(part1(lines))
    println(part2(lines))
}

fun part1(lines : List<String>) : Int {

    val list1 = lines.map { it.split("   ")[0].toInt() }.sorted()
    val list2 = lines.map { it.split("   ")[1].toInt() }.sorted()

    var sum = 0
    
    list1.zip(list2).forEach { (item1, item2) ->
        sum += abs(item2 - item1)
    }

    return sum
} 

fun part2(lines : List<String>) : Int {
    
    val list1 = lines.map { it.split("   ")[0].toInt() }
    val map2 = mutableMapOf<Int, Int>()
    
    lines.forEach { 
        val x = it.split("   ")[1].toInt() 
        map2[x] = map2.getOrDefault(x, 0) + 1
    }

    var sum = 0
    
    list1.forEach {
        sum += it * map2.getOrDefault(it, 0)
    }

    return sum

}
// kotlinc day01_part1.kt -include-runtime -d day01_part1.jar
// java -jar day01_part1.jar