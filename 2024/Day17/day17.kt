import java.io.File
import kotlin.math.pow

data class Computer(var A : Long, var B : Long, var C : Long)

fun toBin(l : Long) : String {
    return java.lang.Long.toBinaryString(l)
}

fun main() {
    val lines = File("input.txt").readLines()

    val a = lines[0].substring(12).toLong()

    val computer = Computer(a, 0L, 0L)

    val instructions = lines[lines.size - 1]
        .substring(9)
        .split(",")
        .map { it.toInt() }

    println(part1(computer, instructions))
    println(part2(instructions))
}

val part1Prints = mutableListOf<String>()

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
            part1Prints.add("$outVal")
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

fun execFastCycle(c : Computer) : Int {
    c.B = (c.A % 8)
    c.B = c.B.xor(2L)
    c.C = (c.A) / 2.0.pow(c.B.toDouble()).toLong()
    c.B = c.B.xor(c.C)
    c.A = c.A / 8
    c.B = c.B.xor(7L)
    return (c.B % 8).toInt()
}

fun part1(computer : Computer, instructions : List<Int>) : String {
    var ip = 0

    while (ip < instructions.size) {
        val opcode = instructions[ip]
        val operand = instructions[ip+1]
        ip = execute(computer, opcode, operand, ip)
    }

    return part1Prints.joinToString(",")
} 

fun part2(instructions : List<Int>) : Long {

    val maps = mutableMapOf<Int, HashSet<Long>>().withDefault{ hashSetOf<Long>() }
    for (i in 0 until 1024) {
        val c = Computer(i.toLong(), 0L, 0L)
        val r = execFastCycle(c)
        if (maps.containsKey(r))
            maps[r]!!.add(i.toLong())
        else 
            maps[r] = hashSetOf(i.toLong())
    }

    var values = maps[instructions[0]]!!
    for (i in 1 until instructions.size) {
        var newValues = hashSetOf<Long>()
        for (a in values) {
            val aBin = toBin(a.toLong())
            val prefixA = toBin((a / (8.0.pow(i)).toLong()).toLong())
            val suffixA = toBin((a % (8.0.pow(i)).toLong()).toLong())

            for (b in maps[instructions[i]]!!) {
                val bBin = toBin(b.toLong())
                val prefixB = toBin((b / 128).toLong())
                val suffixB = toBin((b % 128).toLong())
                if (prefixA == suffixB) {
                    val together = "$bBin${suffixA.padStart(3*i, '0')}"
                    val togetherDec = together.toLong(2)
                    newValues.add(togetherDec)
                }
            }

        }
        values = newValues
    }
    return values.min()
}
