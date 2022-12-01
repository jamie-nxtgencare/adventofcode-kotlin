@file:Suppress("PackageName")

package `2022`

import Project

class DayOne(file: String) : Project {
    private val elves = whitelineSeperatedGrouper(file, { Elf(it) }, { it.toInt() }).sortedByDescending { it.totalCalories }

    class Elf(it: List<Int>) {
        private val foodItems = it
        val totalCalories = it.sum()
    }

    override fun part1(): Any {
        return elves.first().totalCalories
    }

    override fun part2(): Any {
        return elves.take(3).sumOf { it.totalCalories }
    }
}