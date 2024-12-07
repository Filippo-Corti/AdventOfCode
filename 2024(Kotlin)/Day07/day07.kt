import java.io.File

data class Equation (val res : Long, val operands : List<Long>)


fun sum(l1 : Long, l2 : Long) : Long {
    return l1 + l2
}

fun prod(l1 : Long, l2 : Long) : Long {
    return l1 * l2
}
 
fun concat(l1 : Long, l2 : Long) : Long {
    return "$l1$l2".toLong()
}

fun main() {

    val lines = File("input.txt").readLines().map {
        val parts = it.split(": ")
        val res = parts[0].toLong()
        val operands = parts[1].split(" ").map { it.toLong() }
        Equation(res, operands)
    }

    println(part1(lines))
    println(part2(lines))
}

fun part1(lines : List<Equation>) : Long {
   return lines.filter(::isSolveable1).map { it.res }.sum()
} 

fun part2(lines : List<Equation>) : Long  {
   return lines.filter(::isSolveable2).map { it.res }.sum()
}

fun solveWithOp1(eq : Equation, op : (Long, Long) -> Long) : Boolean {
    if (eq.res < 0 || eq.operands.size < 2) return false

    val calc = op(eq.operands[0], eq.operands[1])
    if (eq.operands.size == 2) return eq.res == calc
    
    val newEq = Equation(eq.res, mutableListOf(calc).apply { addAll(eq.operands.subList(2, eq.operands.size)) })

    return isSolveable1(newEq)
}

fun isSolveable1(eq : Equation) : Boolean {
    val operators = listOf(::sum, ::prod)
    if (eq.res < 0) return false

    for (op in operators) {
        if (solveWithOp1(eq, op))
            return true
    }

    return false
}

fun solveWithOp2(eq : Equation, op : (Long, Long) -> Long) : Boolean {
    if (eq.res < 0 || eq.operands.size < 2) return false

    val calc = op(eq.operands[0], eq.operands[1])
    if (eq.operands.size == 2) return eq.res == calc
    
    val newEq = Equation(eq.res, mutableListOf(calc).apply { addAll(eq.operands.subList(2, eq.operands.size)) })

    return isSolveable2(newEq)
}

fun isSolveable2(eq : Equation) : Boolean {
    val operators = listOf(::sum, ::prod, ::concat)
    if (eq.res < 0) return false

    for (op in operators) {
        if (solveWithOp2(eq, op))
            return true
    }

    return false
}

// kotlinc day07.kt -include-runtime -d day07.jar
// java -jar day07.jar