import java.io.File

var gates : MutableMap<String, Boolean> = mutableMapOf()
val connections : MutableList<Connection> = mutableListOf()

data class Connection(val gate1 : String, val gate2: String, val gate3 : String, val op : String) {

    fun execute() {
        gates[gate3] = when (op) {
            "AND" -> gates[gate1]!! && gates[gate2]!!
            "OR" -> gates[gate1]!! || gates[gate2]!!
            "XOR" -> gates[gate1]!! xor gates[gate2]!!
            else -> throw IllegalArgumentException("Unsupported operation: $op")
        }
    }

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
    
    println(gates)
    println(connections)

    println(part1())

}

fun part1() : Long {

    for(conn in connections) {
        conn.execute()
    }

    return gates
        .filter { (k, v) -> k[0] == 'z' }
        .toList()
        .sortedByDescending { it.first } 
        .map { if (it.second) "1" else "0" }
        .joinToString("").toLong(2)


}