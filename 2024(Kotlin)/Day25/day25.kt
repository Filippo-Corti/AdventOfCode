import java.io.File

fun readKey(lines : List<String>) : List<Int> {
    val key = MutableList(5) { 0 }
    var i = lines.size - 1
    while(i >= 0) {
        val line = lines[i]
        for((j, c) in line.withIndex()) {
            if (c == '#') {
                key[j]++
            }
        }
        i--
    }
    return key
}

fun readLock(lines : List<String>) : List<Int> {
    val lock = MutableList(5) { 0 }
    var i = 0
    while(i < lines.size) {
        val line = lines[i]
        for((j, c) in line.withIndex()) {
            if (c == '#') {
                lock[j]++
            }
        }
        i++
    }
    return lock
}

fun main() {

    val keys = mutableListOf<List<Int>>()
    val locks = mutableListOf<List<Int>>()

    val lines = File("input.txt").readLines()

    for (i in 0 until lines.size step 8) {
        if (lines[i][0] == '.') {
            keys.add(readKey(lines.subList(i, i+7)))
        } else {
            locks.add(readLock(lines.subList(i, i+7)))
        }
    }


    println(part1(keys, locks))

}

fun doTheyFit(key : List<Int>, lock : List<Int>) : Boolean {
    for (i in 0 until key.size) {
        if (key[i] + lock[i] > 7) return false
    }
    return true
}

fun part1(keys : List<List<Int>>, locks : List<List<Int>> ) : Long {
    var c = 0L
    for (key in keys) {
        for (lock in locks) {
            if (doTheyFit(key, lock)) c++
        }
    }
    return c
}
