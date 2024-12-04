import java.io.File
import kotlin.math.abs 

fun main() {
    val text: String = File("input.txt").readLines().joinToString("")

    println(part1(text))
    println(part2(text))
}

fun part1(text : String) : Int {
    val regex = Regex("mul\\(([0-9]+),([0-9]+)\\)")
    val matches = regex.findAll(text)
    val couples = matches.map {"(" + it.groupValues[1] + ", " + it.groupValues[2] + ")"}
    val values = matches.map { it.groupValues[1].toInt() * it.groupValues[2].toInt()}
    return values.sum()
} 

fun part2(text : String) : Int {
    val explicitText = "do()" + text + "don't()"
    val regex = Regex("do\\(\\).*?don't\\(\\)")
    val matches = regex.findAll(explicitText)
    val newText = matches.map { it.groupValues[0]}.joinToString("X")

    return part1(newText)
}
// kotlinc day03.kt -include-runtime -d day03.jar
// java -jar day03.jar