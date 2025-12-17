import java.io.File
import kotlin.math.round
import kotlin.math.abs 

data class Button (val dx : Double, val dy : Double)

data class ClawMachine (val button1 : Button, val button2 : Button, val prize : Pair<Double, Double>)

fun main() {

    val machines : MutableList<ClawMachine> = mutableListOf()

    val lines : List<String> = File("input.txt").readLines()
    for (i in 0 until lines.size step 4) {
        val matchesA = Regex("[0-9]+").findAll(lines[i]).map { it.value.toDouble() }.toList()
        val matchesB = Regex("[0-9]+").findAll(lines[i+1]).map { it.value.toDouble() }.toList()
        val matchesPrize = Regex("[0-9]+").findAll(lines[i+2]).map { it.value.toDouble() }.toList()

        machines.add(
            ClawMachine(
                Button(matchesA[0], matchesA[1]),
                Button(matchesB[0], matchesB[1]),
                Pair(matchesPrize[0], matchesPrize[1])
            )
        )

    }


    println(part1(machines.toList()))
    println(part2(machines.toList()))
}

fun part1(machines : List<ClawMachine>) : Long {
    var sum = 0L
    for(m in machines) {
        val bDouble = ((m.prize.second - m.prize.first / m.button1.dx * m.button1.dy) / m.button2.dy) / (1 - m.button2.dx / m.button1.dx * m.button1.dy / m.button2.dy)
        val aDouble = (m.prize.first - bDouble * m.button2.dx) / m.button1.dx 
        val a = round(aDouble)
        val b = round(bDouble)

        if ( a < 0 || b < 0 || a > 100 || b > 100) {
            continue
        }

        if (a * m.button1.dx + b * m.button2.dx == m.prize.first) {
            sum += (a*3 + b*1).toLong()
        }
    }

    return sum
} 

fun part2(machines : List<ClawMachine>) : Long {
    var sum = 0L
    for(m in machines) {
        val prize = Pair(m.prize.first + 10000000000000, m.prize.second + 10000000000000)
        val bDouble = ((prize.second - prize.first / m.button1.dx * m.button1.dy) / m.button2.dy) / (1 - m.button2.dx / m.button1.dx * m.button1.dy / m.button2.dy)
        val aDouble = (prize.first - bDouble * m.button2.dx) / m.button1.dx 
        val a = round(aDouble)
        val b = round(bDouble)

        if ( a < 0 || b < 0) {
            continue
        }

        if (a * m.button1.dx + b * m.button2.dx == prize.first) {
            sum += (a*3 + b*1).toLong()
        }
    }

    return sum
} 

// kotlinc day13.kt -include-runtime -d day13.jar
// java -jar day13.jar