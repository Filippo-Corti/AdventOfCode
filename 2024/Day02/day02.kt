import java.io.File
import kotlin.math.abs 

fun main() {
    val lines: List<String> = File("input.txt").readLines()

    println(part1(lines))
    println(part2(lines))
}

fun part1(lines : List<String>) : Int {

    val lists = lines.map {it.split(" ").map{ it.toInt()} }

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

fun part2(lines : List<String>) : Int {
  
  val lists = lines.map {it.split(" ").map{ it.toInt()} }

    val res = lists.map {
        var dir = if (it[0] > it[1]) 1 else -1
        var x = it[0]
        var removed = false
        for (i in 0 until it.size-1) {
            val diff = dir * (x - it[i+1])
            x = it[i+1]
            if (diff > 3 || diff < 1){
                if (removed)
                    return@map 0
                else {
                    if (i == 0) {
                        dir = if (it[1] > it[2]) 1 else -1
                        x = it[1]
                        removed = true
                    } else {
                        x = it[i]
                        removed = true
                    }
                }
            }
        }
        1
    }.sum()
    
    return res

}
// kotlinc day02.kt -include-runtime -d day02.jar
// java -jar day02.jar