import java.io.File

data class Equation (val res : Long, val operands : List<Long>)

fun main() {

    val lines = File("input.txt").readLines().map {
        val parts = it.split(": ")
        val res = parts[0].toLong()
        val operands = parts[1].split(" ").map { it.toLong() }
        Equation(res, operands)
    }

    //println(lines)

    println(part1(lines))
    println(part2(lines))
}

fun solveWithPlus(eq : Equation) : Boolean {
    //println("Trying to solve with + $eq")
    if (eq.res < 0) return false
    if (eq.operands.size == 2) return eq.res == eq.operands[0] + eq.operands[1]
    val plus = eq.operands[0] + eq.operands[1]
    val plusEq = Equation(eq.res, eq.operands.drop(2).toMutableList().apply { add(0, plus) }.toList())
    return isSolveable1(plusEq)
}

fun solveWithProd(eq : Equation) : Boolean {
   // println("Trying to solve with * $eq")
    if (eq.res < 0) return false
    if (eq.operands.size == 2) return eq.res == eq.operands[0] * eq.operands[1]
    val prod = eq.operands[0] * eq.operands[1]
    
    val prodEq = Equation(eq.res, eq.operands.drop(2).toMutableList().apply { add(0, prod) }.toList())

    return isSolveable1(prodEq)
}

fun isSolveable1(eq : Equation) : Boolean {
    return solveWithPlus(eq) || solveWithProd(eq)
}

fun part1(lines : List<Equation>) : Long {
   return lines.filter(::isSolveable1).map { it.res }.sum()
} 

fun concat(l1 : Long, l2 : Long) : Long {
    return "$l1$l2".toLong()
}

fun solveWithConcat(eq : Equation) : Boolean {
    if (eq.res < 0) return false
    if (eq.operands.size == 2) return eq.res == concat(eq.operands[0], eq.operands[1])
    val conc = concat(eq.operands[0], eq.operands[1])
    
    val prodEq = Equation(eq.res, eq.operands.drop(2).toMutableList().apply { add(0, conc) }.toList())

    return isSolveable2(prodEq)
}

fun solveWithPlus2(eq : Equation) : Boolean {
    //println("Trying to solve with + $eq")
    if (eq.res < 0) return false
    if (eq.operands.size == 2) return eq.res == eq.operands[0] + eq.operands[1]
    val plus = eq.operands[0] + eq.operands[1]
    val plusEq = Equation(eq.res, eq.operands.drop(2).toMutableList().apply { add(0, plus) }.toList())
    return isSolveable2(plusEq)
}

fun solveWithProd2(eq : Equation) : Boolean {
   // println("Trying to solve with * $eq")
    if (eq.res < 0) return false
    if (eq.operands.size == 2) return eq.res == eq.operands[0] * eq.operands[1]
    val prod = eq.operands[0] * eq.operands[1]
    
    val prodEq = Equation(eq.res, eq.operands.drop(2).toMutableList().apply { add(0, prod) }.toList())

    return isSolveable2(prodEq)
}

fun isSolveable2(eq : Equation) : Boolean {
    return solveWithPlus2(eq) || solveWithProd2(eq) || solveWithConcat(eq)
}

fun part2(lines : List<Equation>) : Long  {
   return lines.filter(::isSolveable2).map { it.res }.sum()
}
// kotlinc day07.kt -include-runtime -d day07.jar
// java -jar day07.jar