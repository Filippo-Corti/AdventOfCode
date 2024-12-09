import java.io.File
import kotlin.math.abs 

fun main() {
    val disk : List<Int> = File("input.txt").readLines()[0].toCharArray().map { "$it".toInt() }

    //println(disk)

    println(part1(disk))
    println(part2(disk))
}

fun part1(disk : List<Int>) : Long {
    var sum = 0L

    var leftId = 0
    var rightId = disk.size / 2

    var leftIndex = 0
    var rightIndex = disk.size - 1

    var leftValue = disk[leftIndex]
    var rightValue = disk[rightIndex]

    var currPos = 0L
    var pickingFromLeft = true

    while(leftId < rightId) {
        if (pickingFromLeft) {
            while(leftValue > 0) {
                sum += leftId.toLong() * currPos
                currPos++
                leftValue--
            }
            leftId++
        } else {
            while(leftValue > 0) {
                sum += rightId.toLong() * currPos
                currPos++
                leftValue--
                rightValue--
                if (rightValue == 0) {
                    rightIndex-=2
                    rightValue = disk[rightIndex]
                    rightId--
                    if (leftId >= rightId)
                        break
                }
            }
        }
        leftIndex++
        leftValue = disk[leftIndex]
        pickingFromLeft = !pickingFromLeft
    }

    while (rightValue > 0) {
        sum += rightId.toLong() * currPos
        currPos++
        rightValue--
    }


    return sum
} 

fun part2(disk : List<Int>) : Long {

    var diskCpy = disk.toMutableList()

    var sum = 0L

    var leftId = 0
    var rightId = disk.size / 2
    
    var rightIndex = disk.size - 1

    var currPos = 0L
    var pickingFromLeft = true

    for(i in 0 until disk.size) {
        var space = diskCpy[i]
        if (pickingFromLeft) {
            if (space == 0) currPos += disk[i].toLong()
            while(space > 0) {
                //println("$space: Counting non moved file with ID=$leftId * $currPos")
                sum += leftId.toLong() * currPos
                currPos++
                space--
                diskCpy[i]--
            }
            leftId++
        } else {
            loop@ while(space > 0) {
                //println("$space: Finding a file that fits")
                //Find a file that fits
                while(!(diskCpy[rightIndex] <= space && diskCpy[rightIndex] != 0)) { 
                    //println("Id $rightId is not ok ${diskCpy[rightIndex]}")
                    rightIndex-=2
                    if (rightIndex < 0) {
                        rightIndex = diskCpy.size - 1
                        rightId = disk.size / 2
                        currPos += space.toLong()
                        break@loop
                    }
                    rightId--
                }

                //println("Found file with id $rightId")

                //Position file
                while (diskCpy[rightIndex] > 0) {
                    //println("$space: Counting moved file with ID=$rightId * $currPos")
                    sum += rightId.toLong() * currPos
                    currPos++
                    diskCpy[rightIndex]--
                    space--
                }

                rightIndex = disk.size - 1
                rightId = disk.size / 2
            }


        }
        pickingFromLeft = !pickingFromLeft
    }

    return sum
}
// kotlinc day09.kt -include-runtime -d day09.jar
// java -jar day09.jar