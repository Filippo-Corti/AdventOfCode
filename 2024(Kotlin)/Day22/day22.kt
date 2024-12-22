import java.io.File
import kotlin.math.max

data class ChangeSequence(val a : Int, val b : Int, val c : Int, val d : Int)

fun nextNumber(n : Long) : Long {
    var curr = n
    curr = (curr * 64L).xor(curr) % 16777216
    curr = (curr / 32L).xor(curr) % 16777216
    curr = (curr * 2048L).xor(curr) % 16777216

    return curr
}

fun getChangesSequences(n : Long) : Map<ChangeSequence, Int> {
    var map = mutableMapOf<ChangeSequence, Int>().withDefault { 0 }
    var changes = ArrayDeque<Int>()
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
            if (!map.containsKey(changeSeq))
                map.put(changeSeq, price)
        }
        curr = next
    }
    return map
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

    println(part1(numbers))
    println(part2(numbers))
}

fun part1(numbers : List<Long>) : Long {
    return numbers
        .map { get2000thNumber(it) }
        .sum()
}

fun part2(numbers : List<Long>) : Int {
    val sequencesMaps = numbers.map { getChangesSequences(it) }
    val keys = sequencesMaps.map { it.keys }.flatMap { it }.toHashSet()

    println("Need to check ${keys.size}")

    var maxRes = 0
    for (changeSeq in keys) {
        val res = sequencesMaps.map { it.getValue(changeSeq) }.sum()
        maxRes = max(maxRes, res)
    }

    return maxRes
}