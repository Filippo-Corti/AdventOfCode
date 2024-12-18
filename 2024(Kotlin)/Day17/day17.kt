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
            print("$outVal,")
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
       // println("Computer is $computer")
        val opcode = instructions[ip]
        val operand = instructions[ip+1]

       // println("Executing Opcode=$opcode and operand=$operand")
        ip = execute(computer, opcode, operand, ip)

    }

    //println("Computer is $computer")

} 
