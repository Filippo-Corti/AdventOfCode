import java.io.File
import kotlin.math.abs 

fun main() {
    val lines: List<String> = File("input.txt").readLines()

    val lists = lines.map {it.split(" ").map{ it.toInt()} }

    println(part1(lists))
    println(part2(lists))
}

fun part1(lists : List<List<Int>>) : Int {

    val res = lists.map {
        val dir = if (it[0] > it[1]) 1 else -1
        for (i in 0 until it.size-1) {
            val diff = dir * (it[i] - it[i+1])
            if (diff > 3 || diff < 1){
                return@map 0
            }
        }
        1
    }.sum()
    
    return res

} 

// This is very bad...
fun part2(lists : List<List<Int>>) : Int {

    val res = lists.sumOf {
        var sum = 0
        for (i in it.indices) {
            var ok = false
            val listWithout = it.filterIndexed {idx, x -> idx != i}
            val isOk = part1(listOf(listWithout)) == 1
            //println("$listWithout $isOk")
            ok = ok || isOk

            if (ok) return@sumOf 1
            0
        }
        sum
    }
    
    return res

}
// kotlinc day02.kt -include-runtime -d day02.jar
// java -jar day02.jar