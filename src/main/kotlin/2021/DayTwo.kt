@file:Suppress("PackageName")
package `2021`

import Project

class DayTwo(file: String, isTest: Boolean = false) : Project(file, isTest) {
    private val instructions = mapFileLines(file) { Instruction(it) }

    override suspend fun part1(): Any {
        return go(1)
    }

    override suspend fun part2(): Any {
        return go(2)
    }

    private fun go(part: Int): Any {
        var output = Triple(0, 0, 0)
        instructions.forEach { output = it.apply(output, part) }
        return output.first * output.second
    }
}

class Instruction(it: String) {
    private val instructionStr = it.split(" ")
    private val instruction = instructionStr[0]
    private val amount: Int = instructionStr[1].toInt()

    fun apply(coord: Triple<Int, Int, Int>, part: Int): Triple<Int, Int, Int> {
        return when(instruction) {
            "forward" -> Triple(coord.first.plus(amount), if (part == 1) coord.second else coord.second.plus(coord.third.times(amount)), coord.third)
            "up" -> Triple(coord.first, if (part == 1) coord.second.minus(amount) else coord.second, coord.third.minus(amount))
            "down" -> Triple(coord.first, if (part == 1) coord.second.plus(amount) else coord.second, coord.third.plus(amount))
            else -> Triple(coord.first, coord.second, coord.third)
        }
    }
}
