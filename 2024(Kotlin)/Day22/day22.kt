import java.io.File

fun toBin(l : Long) : String {
    return java.lang.Long.toBinaryString(l)
}

fun nextNumber(n : Long) : Long {
    //println("Number is $n: ${toBin(n)}")

    var curr = n
    curr = (curr * 64L).xor(curr) % 16777216
    curr = (curr / 32L).xor(curr) % 16777216
    curr = (curr * 2048L).xor(curr) % 16777216

    //println("Output is $curr: ${toBin(curr)}")
    return curr
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
}

fun part1(numbers : List<Long>) : Long {
    return numbers
        .map { get2000thNumber(it) }
        .sum()
}