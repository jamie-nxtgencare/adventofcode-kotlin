@file:Suppress("PackageName")

package `2023`

import Project

class DayFifteen(file: String) : Project() {
    val input = getLines(file).flatMap { it.split(",").filter { it.isNotBlank() }}.map { it.split("").filter { it.isNotBlank() }.map { it[0].code.toDouble() }.toMutableList()}.toMutableList()
    override fun part1(): Any {
        return input.map {
            it[0] = it[0] * 17.0 % 256.0
            it.reduce { a,b -> (a + b) * 17.0 % 256.0 }
        }.sum()
    }

    override fun part2(): Any {
        return -1
    }
}