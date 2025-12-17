import java.io.File

var gates : MutableMap<String, Boolean> = mutableMapOf()
val connections : MutableList<Connection> = mutableListOf()

data class Connection(val gate1 : String, val gate2: String, val gate3 : String, val op : String) {

    fun execute() {
        println("$gate1 $op $gate2 -> $gate3")
        gates[gate3] = when (op) {
            "AND" -> gates[gate1]!! && gates[gate2]!!
            "OR" -> gates[gate1]!! || gates[gate2]!!
            "XOR" -> gates[gate1]!! xor gates[gate2]!!
            else -> throw IllegalArgumentException("Unsupported operation: $op")
        }
    }

}

fun bitSum(b1 : String, b2 : String, b3 : String? = null) : Pair<String, String> {
    return when {
        b1 == "0" && b2 == "0" && b3 == "0"-> "0" to "0"
        b1 == "0" && b2 == "0" && b3 == "1"-> "0" to "1"
        b1 == "0" && b2 == "1" && b3 == "0"-> "0" to "1"
        b1 == "0" && b2 == "1" && b3 == "1"-> "1" to "0"
        b1 == "1" && b2 == "0" && b3 == "0"-> "0" to "1"
        b1 == "1" && b2 == "0" && b3 == "1"-> "1" to "0"
        b1 == "1" && b2 == "1" && b3 == "0"-> "1" to "0"
        b1 == "1" && b2 == "1" && b3 == "1"-> "1" to "1"
        else ->  throw IllegalArgumentException("Illegal Arguments $b1 $b2 $b3")
    }
}

fun calcBit(gateX : String, gateY : String, gatePrev : String = "") : String {
    val rightGates = hashSetOf(gateX, gateY, gatePrev)
    val executed = hashSetOf<Connection>()
    val usableGates = hashSetOf(gateX, gateY, gatePrev)

    while(true) {
        val conns = connections.filter { !executed.contains(it) && usableGates.contains(it.gate1) && usableGates.contains(it.gate2) }
        if (conns.isEmpty()) break
        conns.forEach { 
            it.execute()
            usableGates.add(it.gate3)
            rightGates.add(it.gate1)
            rightGates.add(it.gate2)
            executed.add(it)
        }
    }

    println("------")

    val result = usableGates subtract rightGates
    return result.find { it[0] != 'z' } ?: "z45"
} 

fun main() {

    val (inputs, wires) = File("input.txt").readLines()
        .let { lines -> lines.takeWhile { it.isNotEmpty() } to lines.dropWhile { it.isNotEmpty() }.drop(1) }


    gates = inputs.map { 
        val x = it.split(": ")
        x[0] to (x[1] == "1")
    }.toMap().toMutableMap()

    val gatesUsed = gates.keys.toHashSet()
    val wiresQueue = ArrayDeque<String>()
    wires.forEach { wiresQueue.add(it) }

    while(!wiresQueue.isEmpty()) {
        val wire = wiresQueue.removeFirst()
        val parts = wire.split(" -> ")
        val ops = parts[0].split(" ")

        if (!gatesUsed.contains(ops[0]) || !gatesUsed.contains(ops[2])) {
            wiresQueue.add(wire)
            continue
        }

        val conn = Connection(ops[0], ops[2], parts[1], ops[1])
        connections.add(conn)
        gatesUsed.add(parts[1])
    }

    //println(part1())
    println(part2()) //cqr,ncd,nfj,qnw,vkg,z15,z20,z37
}

fun part1() : Long {

    connections.forEach { it.execute() }

    return gates
        .filter { (k, v) -> k[0] == 'z' }
        .toList()
        .sortedByDescending { it.first } 
        .map { if (it.second) "1" else "0" }
        .joinToString("").toLong(2)
}

fun part2() : String {
    var res = ""
    var carryover = "0"
    var invertedLabels = mutableListOf<String>()
    for (i in 0 until 45) {
        val iString = "$i".padStart(2, '0')
        res = calcBit("x$iString", "y$iString", res)
        val x = if (gates["x$iString"]!!) "1" else "0"
        val y = if (gates["y$iString"]!!) "1" else "0"
        val z = if (gates["z$iString"]!!) "1" else "0"
        val nextCarryover = if (gates[res]!!) "1" else "0"

        val expected = bitSum(x, y, carryover)

        //println("$i: $x + $y = $z con riporto $res = $nextCarryover")

        if (z != expected.second || nextCarryover != expected.first) {
             println("Errore: found ($nextCarryover, $z) but expected $expected")
            carryover = expected.first
            gates[res] = expected.first == "1"
            gates["z$iString"] = expected.second == "1"
            invertedLabels.add("z$iString")
            invertedLabels.add(res)
        } else {
            carryover = nextCarryover
        }
        // }
    }

    val xBin = gates
        .filter { (k, v) -> k[0] == 'x' }
        .toList()
        .sortedByDescending { it.first } 
        .map { if (it.second) "1" else "0" }
        .joinToString("")

    val x = xBin.toLong(2)

    val yBin = gates
        .filter { (k, v) -> k[0] == 'y' }
        .toList()
        .sortedByDescending { it.first } 
        .map { if (it.second) "1" else "0" }
        .joinToString("")

    val y = yBin.toLong(2)

    val zBin = gates
        .filter { (k, v) -> k[0] == 'z' }
        .toList()
        .sortedByDescending { it.first } 
        .map { if (it.second) "1" else "0" }
        .joinToString("")
    
    val z = zBin.toLong(2)

    //println("x = 0$xBin - $x\ny = 0$yBin - $y\nz = $zBin - $z. Result is correct? ${z == x+y}")

    return invertedLabels.sorted().joinToString(",")
}
