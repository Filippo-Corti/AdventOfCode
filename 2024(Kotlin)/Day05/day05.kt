import java.io.File
import kotlin.math.abs 

fun main() {
    val (rules, updates) = File("input.txt").readLines()
        .let { lines -> lines.takeWhile { it.isNotEmpty() } to lines.dropWhile { it.isNotEmpty() }.drop(1) }

    println(part1(rules, updates))
    println(part2(rules, updates))
    //println(part2(text))
}

// Uses Kahn's Algorithm for Topological Sort
fun buildCorrectSequence(rules : List<String>, valuesToConsider : List<Int>) : List<Int> {
    //Build Adjacency Lists
    val adjacencies = mutableMapOf<Int, MutableList<Int>>()
    val inDegrees = mutableMapOf<Int, Int>()

    for (rule in rules) {
        val nums = rule.split("|").map { it.toInt() }
        val from = nums[0]
        val to = nums[1]

        if (!valuesToConsider.contains(from) || !valuesToConsider.contains(to)) continue

        adjacencies.computeIfAbsent(from) { mutableListOf() }.add(to)

        inDegrees[to] = inDegrees.getOrDefault(to, 0) + 1
        inDegrees.putIfAbsent(from, 0) 
    }

    val queue = ArrayDeque<Int>()
    val sequence = mutableListOf<Int>()

    inDegrees.forEach { (node, degree) ->
        if (degree == 0) queue.add(node)
    }

    //println("Queue is $queue ${queue.size} ${queue.isEmpty()}")
    // for ((k, v) in adjacencies) {
    //     println("Key $k: $v")
    // }

    while (!queue.isEmpty()) {
        val current = queue.removeFirst()
        sequence.add(current)

        for (neighbour in adjacencies[current].orEmpty()) {
            inDegrees[neighbour] = inDegrees[neighbour]!! - 1
            if (inDegrees[neighbour] == 0) 
                queue.add(neighbour)
        }

    }

    return sequence
}

fun part1(rules : List<String>, updates : List<String>) : Int {
    var sum = 0
    for (updateString in updates) {
        val update = updateString.split(",").map{ it.toInt() }
        val order = buildCorrectSequence(rules, update)
        if (isValidUpdate(order, update)) {
            sum += update[update.size / 2]
        }
    }
    return sum
} 

fun isValidUpdate(order : List<Int>, update : List<Int>) : Boolean {
    var lastIndex = order.indexOf(update[0])
    for (i in 1 until update.size) {
        val currentIndex = order.indexOf(update[i])
        if (currentIndex <= lastIndex) 
            return false
        lastIndex = currentIndex
    }
    return true
}

fun part2(rules : List<String>, updates : List<String>) : Int {
     var sum = 0
    for (updateString in updates) {
        val update = updateString.split(",").map{ it.toInt() }
        val order = buildCorrectSequence(rules, update)
        if (!isValidUpdate(order, update)) {
            sum += order[order.size / 2]
        }
    }
    return sum
}
// kotlinc day05.kt -include-runtime -d day05.jar
// java -jar day05.jar