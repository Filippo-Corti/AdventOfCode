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

    //part1(computer, instructions)
    part2(instructions)
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
            print("\n---\nPRINT $outVal\n---\n")
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

        println("Computer is ${computer} or")
        print("${java.lang.Long.toBinaryString(computer.A)}\t")
        print("${java.lang.Long.toBinaryString(computer.B)}\t")
        println("${java.lang.Long.toBinaryString(computer.C)}")
    }
} 

fun testExec(c : Computer, expected : List<Int>) : Int {
    if (expected.size == 0)
        return 0
    
    val computer = Computer(c.A, c.B, c.C)
    val res = execFastCycle(computer)
    if (expected[0] != res)
        return expected.size
    
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

fun smallestAToPrintX(x : Int, prefix : String = "") : Pair<Int, Long> {
    var currs = ArrayDeque<String>()
    currs.add("0$prefix")
    currs.add("1$prefix")
    while(true) {
        val curr = currs.removeFirst()
        val withZero = "0$curr"
        var c = Computer(withZero.toLong(2),0L, 0L)
        val withZeroRes = execFastCycle(c)
        if (withZeroRes == x) 
            return withZero.toInt(2) to c.A
        else {
            currs.add("0$withZero")
            currs.add("1$withZero")
        }
        
        val withOne = "1$curr"
        c = Computer(withOne.toLong(2),0L, 0L)
        val withOneRes = execFastCycle(c)
        if (withOneRes == x) 
            return withOne.toInt(2) to c.A
        else {
            currs.add("0$withOne")
            currs.add("1$withOne")
        }
    }
}

// A is between 8^15 and 8^16
// The output of a cycle depends exclusively on A, in particolar,
// on the last 10 binary digits of its value
fun part2(instructions : List<Int>) : Long {

    val maps = mutableMapOf<Int, HashSet<Int>>().withDefault{ hashSetOf<Int>() }

    for (i in 0 until 1024) {
        val c = Computer(i.toLong(), 0L, 0L)
        val r = execFastCycle(c)
        if (maps.containsKey(r))
            maps[r]!!.add(i)
        else 
            maps[r] = hashSetOf(i)
    }

    for ((k, v) in maps) {
        println("There are ${v.size} values that print $k")
    }

    // val inn = 29
    // val binIn = java.lang.Long.toBinaryString(inn.toLong())

    // val relevantPart = if (binIn.length >= 10) binIn.substring(binIn.length - 10) else binIn
    // val relevantPartInt = relevantPart.toInt(2)

    // println("First output for $inn ($binIn) depends on $relevantPart and is ${outputs[relevantPartInt]}")

    //Supponiamo di conoscere tutti i numeri D tra 0 e 1024 che fanno stampare 2
    //Supponiamo di conoscere tutti i numeri Q tra 0 e 1024 che fanno stampare 4
    //Supponiamo di conoscere tutti i numeri U tra 0 e 1024 che fanno stampare 1
    //Tutti i numeri che fanno stampare prima 2 e poi 4 e poi 1 sono?
    // Quelli della forma AAABBBXXXXYYYZZZ dove
    // AAABBBXXXX sta nell'insieme U
    // BBBXXXXYYY sta nell'insieme Q
    // XXXYYYZZZ sta nell'insieme D

    // val central = ""
    // val bin = "1111111${central}111"

    // val res = execFastCycle(Computer(bin.toLong(2), 0L, 0L))
    // println("Risultato: $res for ${bin.toLong(2)}")

    // println(smallestAToPrintX(2)) //111
    
    // println(smallestAToPrintX(4, "")) //001
    
    // println(smallestAToPrintX(1, "")) //100

    // println(smallestAToPrintX(2, "")) //111

    // for (a in 0 until 1000) {
    //     if (testExec(Computer(a.toLong(), 0L, 0L), listOf(2, 0)) == 0) {
    //         println("$a is ok")
    //         break
    //     }
    // }

    return 0
}
