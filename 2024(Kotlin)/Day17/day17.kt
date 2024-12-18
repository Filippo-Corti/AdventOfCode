import java.io.File
import kotlin.math.pow

data class Computer(var A : Long, var B : Long, var C : Long)

fun main() {
    val lines = File("input.txt").readLines()

    val a = lines[0].substring(12).toLong()

    val computer = Computer(a, 0L, 0L)

    val instructions = lines[lines.size - 1]
        .substring(9)
        .split(",")
        .map { it.toInt() }

    part1(computer, instructions)
    //part2(instructions)
}

fun evaluateComboOperand(computer : Computer, operand : Int) : Long {
    return when (operand) {
        in 0..3 -> operand.toLong()
        4 -> computer.A
        5 -> computer.B
        6 -> computer.C
        else -> throw Error("Invalid Operand")
    }
}

fun execute(computer : Computer, opcode : Int, operand : Int, ip : Int) : Int {
    when (opcode) {
        0 -> { // ADV
            computer.A = computer.A / 2.0.pow(evaluateComboOperand(computer, operand).toDouble()).toLong()
        }
        1 -> { // BXL
            computer.B = computer.B.xor(operand.toLong())
        }
        2 -> { // BST
            computer.B = evaluateComboOperand(computer, operand) % 8
        }
        3 -> { // JNZ
            if (computer.A != 0L) {
                return operand
            }
        }
        4 -> { // BXC
            computer.B = computer.B.xor(computer.C)
        }
        5 -> { // OUT
            val outVal = evaluateComboOperand(computer, operand) % 8
            print("\n---\n$outVal - $computer\n---\n")
        }
        6 -> { // BVD
            computer.B = computer.A / 2.0.pow(evaluateComboOperand(computer, operand).toDouble()).toLong()
        }
        7 -> { //CBD
            computer.C = computer.A / 2.0.pow(evaluateComboOperand(computer, operand).toDouble()).toLong()
        }
    }
    return ip + 2
}

fun part1(computer : Computer, instructions : List<Int>) {
    var ip = 0

    while (ip < instructions.size) {
        val opcode = instructions[ip]
        val operand = instructions[ip+1]

        print("($ip) Executing $opcode, $operand - ")

        ip = execute(computer, opcode, operand, ip)

        println("Result is $computer")
    }
} 

fun testExec(c : Computer, expected : List<Int>) : Boolean {
    if (expected.size == 0)
        return true
    
    val computer = Computer(c.A, c.B, c.C)
    val res = execFastCycle(computer)
    if (expected[0] != res)
        return false
    
    if (expected.size == 11)
        println("Passed 5 Levels! ${c.A}")

    return testExec(computer, expected.subList(1, expected.size))
}

fun execFastCycle(c : Computer) : Int {
    c.B = (c.A % 8)
    c.B = c.B.xor(2L)
    c.C = (c.A) / 2.0.pow(c.B.toDouble()).toLong()
    c.B = c.B.xor(c.C)
    c.A = c.A / 8
    c.B = c.B.xor(7L)
    return (c.B % 8).toInt()
}

fun inverseFastCycle(c : Computer) : Int {
    c.B = c.B.xor(7L)
    c.A = c.A * 8
    c.B = c.B.xor(c.C)
    //c.C = ???
    c.B = (c.B).xor(2L)
    //c.B = ??? c.A / (8 * k)
    return (c.B % 8).toInt()
}

// A is between 8^15 and 8^16
fun part2(instructions : List<Int>) : Long {

    val min = 8.0.pow(15.0).toLong()
    val max = 8.0.pow(16.0).toLong()
    
    val c = Computer(1881L, 117L, 117L)

    //while(c.A != 0L) {
        // val p = inverseFastCycle(c)
        // println("Computer is $c - printed $p")
    //}

    for(a in min until max) {
        val c = Computer(a, 0L, 0L)
        if (testExec(c, instructions)) {
            return a
        }
        
        //println("$a/$max")
    }
    return 0
}
