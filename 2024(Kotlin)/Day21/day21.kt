import java.io.File

typealias Position = Pair<Int, Int>

val dirs = listOf(
    0 to 1,
    0 to -1,
    1 to 0,
    -1 to 0
)

val dirChars = mapOf(
    (0 to 1) to ">",
    (0 to -1) to "<",
    (1 to 0) to "v",
    (-1 to 0) to "^"
)

val longString = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"

class NumPad(
    val name : String,
    val passResultTo : (String) -> String = { it }
) {

    companion object {

        val states = mapOf<Char, Position>(
            '0' to Position(3, 1), 
            '1' to Position(2, 0), 
            '2' to Position(2, 1), 
            '3' to Position(2, 2), 
            '4' to Position(1, 0), 
            '5' to Position(1, 1), 
            '6' to Position(1, 2), 
            '7' to Position(0, 0), 
            '8' to Position(0, 1), 
            '9' to Position(0, 2), 
            'A' to Position(3, 2)
        ).withDefault { Position(3, 2) }

        val positions = states.entries.associate { (key, value) -> value to key }

    }

    fun allPaths(start : Char, target : Char) : HashSet<String> {
        
    }

    fun moveTo(curr : Char, target : Char, depth : Int = 0) : String {
        if (target == curr) return passResultTo("A")
        if (depth > 5) return longString

        val currPos = states.getValue(curr)
        val targetPos = states.getValue(target)
        var bestNext = Position(0, 0)
        var bestStr = longString
        for (dir in dirs) {
            val next = Position(currPos.first + dir.first, currPos.second + dir.second)
            if (!positions.containsKey(next)) continue
            val str = passResultTo(dirChars[dir]!!)
            if (str.length < bestStr.length) {
                bestNext = next
                bestStr = str
            }
        }

        println("I am $name and I believe the best way to get from $curr to $target is $bestNext. String is $bestStr")

        curr = positions[bestNext]!!
        return bestStr + moveTo(positions[bestNext]!!, target, depth + 1)
    }

    fun moveTo(target : String) : String {
        var str = ""
        for (c in target) {
            str += moveTo(c)
        }
        return str
    }

}

class DirPad(
    val name : String,
    val passResultTo : (String) -> String = { it }
) {

    var curr: Char = 'A'
        set(value) {
            field = value
            currPos = states.getValue(value)
        }

    var currPos: Position = states.getValue(curr)

    companion object {

        val states = mapOf<Char, Position>(
            '^' to Position(0, 1), 
            'A' to Position(0, 2),
            '<' to Position(1, 0), 
            'v' to Position(1, 1), 
            '>' to Position(1, 2)
        ).withDefault { Position(0, 2) }

        val positions = states.entries.associate { (key, value) -> value to key }

    }

    fun moveTo(target : Char, depth : Int = 0) : String {
        if (target == curr) return passResultTo("A")
        if (depth > 5) return longString

        val targetPos = states.getValue(target)
        var bestNext = Position(0, 0)
        var bestStr = longString
        for (dir in dirs) {
            val next = Position(currPos.first + dir.first, currPos.second + dir.second)
            if (!positions.containsKey(next)) continue
            val str = passResultTo(dirChars[dir]!!)
            if (str.length < bestStr.length) {
                bestNext = next
                bestStr = str
            }
        }

        println("I am $name and I believe the best way to get from $curr to $target is $bestNext. String is $bestStr")

        curr = positions[bestNext]!!
        return bestStr + moveTo(target, depth + 1)
    }


    fun moveTo(target : String) : String {
        var str = ""
        for (c in target) {
            str += moveTo(c)
        }
        return str
    }

}

fun main() {

    val lines = File("input.txt").readLines()

    println(part1(lines))
}

fun part1(sequences : List<String>) : Int {
    var sum = 0
    for(seq in sequences) {
        val dirPad2 = DirPad("DirPad1")
        val dirPad1 = DirPad(
            name = "DirPad2",
            passResultTo = { dirPad2.moveTo(it) }
        )
        val numPad = NumPad(
            name = "NumPad",
            passResultTo = { dirPad1.moveTo(it) }
        )

        val movedTo = numPad.moveTo(seq)

        println("Sequence: $seq. Found: $movedTo")

        sum += movedTo.length * seq.substring(0, 3).toInt()
    }
    return sum
}

// v<A<AA>>^AvAA<^A>Av<<A>>^AvA^Av<<A>>^AAvA<A>^A<A>Av<A<A>>^AAAvA<^A>A
// <vA<AA>>^AvAA<^A>A<v<A>>^AvA^A<vA>^A<v<A>^A>AAvA^A<v<A>A>^AAAvA<^A>A

// v<<A>>^AAAvA^Av<A<AA>>^AvAA<^A>Av<A<A>>^AAAvA<^A>Av<A>^A<A>A
// <v<A>>^AAAvA^A<vA<AA>>^AvAA<^A>A<v<A>A>^AAAvA<^A>A<vA>^A<A>A

// v<<A>>^Av<A<A>>^AAvAA<^A>Av<<A>>^AAvA^Av<A>^AA<A>Av<A<A>>^AAAvA<^A>A
// <v<A>>^A<vA<A>>^AAvAA<^A>A<v<A>>^AAvA^A<vA>^AA<A>A<v<A>A>^AAAvA<^A>A

// v<<A>>^AAv<A<A>>^AAvAA<^A>Av<A>^A<A>Av<A>^A<A>Av<A<A>>^AAvA<^A>A
// <v<A>>^AA<vA<A>>^AAvAA<^A>A<vA>^A<A>A<vA>^A<A>A<v<A>A>^AAvA<^A>A

// v<<A>>^AvA^Av<<A>>^AAv<A<A>>^AAvAA<^A>Av<A>^AA<A>Av<A<A>>^AAAvA<^A>A
// <v<A>>^AvA^A<vA<AA>>^AAvA<^A>AAvA^A<vA>^AA<A>A<v<A>A>^AAAvA<^A>A