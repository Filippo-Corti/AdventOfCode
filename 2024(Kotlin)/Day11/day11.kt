import java.io.File

fun main() {
    var stones = File("input.txt").readLines()[0].split(" ").map { it.toLong() }.toMutableList()

    //println(part1(stones.toList().toMutableList()))
    println(part2(stones))
}

fun part1(stones: MutableList<Long>): Int {

    val map : MutableMap<Long, List<Long>> = mutableMapOf()
    
    for (i in 0 until 5) {
        val oldStones = stones.toList()
        stones.clear()
        stones.addAll(oldStones.map { blinkNTimes(map, it, 5)}.flatten())
        println("$i/5")
    }

    return stones.size
}

fun blinkNTimes(map : MutableMap<Long, List<Long>>, stone : Long, n : Int) : List<Long> {

    if (map.containsKey(stone)) {
        //println("Skipped with map")
        return map[stone]!!.toList()
    }

    var stones = mutableListOf(stone)
    for (i in 0 until n) {
        val oldStones = stones.toList()
        var addedValues = 0
        for (idx in oldStones.indices) {
            val stone = oldStones[idx]
            val stoneString = "$stone"
            if (stone == 0L) {
                stones[idx + addedValues] = 1
            } else if (stoneString.length % 2 == 0) {
                val newStone1 = stoneString.substring(0, stoneString.length / 2).toLong()
                val newStone2 = stoneString.substring(stoneString.length / 2).toLong()
                stones[idx + addedValues] = newStone1
                stones.add(idx + addedValues + 1, newStone2)
                addedValues++
            } else {
                stones[idx + addedValues] = stone * 2024
            }
        }
    }

    map[stone] = stones.toList()

    return stones
}

var c = 0

fun blinkNTimesList(map : MutableMap<Long, List<Long>>, stones : MutableList<Long>, count : Int, n : Int) : Long {
    if (count == 0) 
        return stones.size.toLong()

    var sum = 0L
    for (stone in stones) {
        val list = blinkNTimes(map, stone, n).toMutableList()
        sum += blinkNTimesList(map, list, count-1, n)
        if (count == 3) {
            println("$c/212655")
            c++
        }
    }
    return sum
}

fun part2(stones: MutableList<Long>): Long {

    val map : MutableMap<Long, List<Long>> = mutableMapOf()

    return blinkNTimesList(map, stones, 5, 15)
}



// kotlinc day11.kt -include-runtime -d day11.jar
// java -jar day11.jar