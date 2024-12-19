@file:Suppress("PackageName")

package `2022`

import Project

class DayOne(file: String, isTest: Boolean = false) : Project(file, isTest) {
    private val elves = whitelineSeperatedGrouper(file, { Elf(it) }, { it.toInt() }).sortedByDescending { it.totalCalories }

    class Elf(it: List<Int>) {
        private val foodItems = it
        val totalCalories = it.sum()
    }

    override suspend fun part1(): Any {
        return elves.first().totalCalories
    }

    override suspend fun part2(): Any {
        return elves.take(3).sumOf { it.totalCalories }
    }
}