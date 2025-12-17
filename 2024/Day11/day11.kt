import java.io.File

fun main() {
    var stones = File("input.txt").readLines()[0].split(" ").map { it.toLong() }.toMutableList()

    println(part1(stones))
    println(part2(stones))
}

val mem : MutableMap<Pair<Long, Int>, Long> = mutableMapOf()

fun part1(stones: MutableList<Long>): Long {
    return stones.map{ blinkN(it, 25) }.sum().toLong()
}

fun part2(stones: MutableList<Long>): Long {
    return stones.map{ blinkN(it, 75) }.sum().toLong()
}

fun blinkN(stone : Long, n : Int) : Long {
    if (mem.containsKey(stone to n)) {
        return mem[stone to n]!!
    }

    val blinkedList = blink(stone)
    var res : Long
    if (n == 1) {
        res = blinkedList.size.toLong()
    } else {
        res = blinkedList.map{ blinkN(it, n-1) }.sum().toLong()
    }
        
    mem[stone to n] = res
    return res
}

fun blink(stone : Long) : List<Long> {
    val stoneString = "$stone"
    if (stone == 0L) {
        return listOf(1)
    } else if (stoneString.length % 2 == 0) {
        val newStone1 = stoneString.substring(0, stoneString.length / 2).toLong()
        val newStone2 = stoneString.substring(stoneString.length / 2).toLong()
        return listOf(newStone1, newStone2)
    } else {
        return listOf(stone * 2024L)
    }
}

// kotlinc day11.kt -include-runtime -d day11.jar
// java -jar day11.jar