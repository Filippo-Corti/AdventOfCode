import java.io.File
import kotlin.math.max

data class ChangeSequence(val a : Int, val b : Int, val c : Int, val d : Int)

var numbersSize : Int = 0

fun nextNumber(n : Long) : Long {
    var curr = n
    curr = (curr * 64L).xor(curr) % 16777216
    curr = (curr / 32L).xor(curr) % 16777216
    curr = (curr * 2048L).xor(curr) % 16777216

    return curr
}

fun updateChangesSequences(n : Long, changesMap : MutableMap<ChangeSequence, IntArray>, idx : Int) {
    var changes = ArrayDeque<Int>()
    var foundSequences = hashSetOf<ChangeSequence>()
    var curr = n
    for (i in 0 until 2000) {
        val next = nextNumber(curr)
        val price = next.toInt() % 10
        val change = price - (curr.toInt() % 10)
        changes.add(change)
        if (changes.size > 4) 
            changes.removeFirst()
        
        if (changes.size == 4) {
            val l = changes.toList()
            val changeSeq = ChangeSequence(l[0], l[1], l[2], l[3])
            if (!foundSequences.contains(changeSeq)) {
                val array = changesMap.getOrPut(changeSeq) { IntArray(numbersSize) { 0 } }
                array[idx] = price
                foundSequences.add(changeSeq)
            }
        }
        curr = next
    }
}

fun get2000thNumber(n : Long) : Long {
    var curr = n
    for (i in 0 until 2000) {
        curr = nextNumber(curr)
    }
    return curr
}

fun main() {

    val numbers = File("input.txt").readLines().map { it.toLong() }
    numbersSize = numbers.size

    println(part1(numbers))
    println(part2(numbers))
}

fun part1(numbers : List<Long>) : Long {
    return numbers
        .map { get2000thNumber(it) }
        .sum()
}

fun part2(numbers : List<Long>) : Int {
    val changesMap = mutableMapOf<ChangeSequence, IntArray>().withDefault { IntArray(numbersSize) }
    numbers.forEachIndexed { i, v -> updateChangesSequences(v, changesMap, i) }

    return changesMap.map { (_, v) -> v.sum() }.max()
}