@file:Suppress("PackageName")

package `2019`

import Project

class DayOne(file: String, isTest: Boolean = false) : Project(file, isTest) {
    private val map = getIntLinesToExistsBoolean(file)

    override suspend fun part1(): Any {
        return -1
    }

    override suspend fun part2(): Any {
        return -1
    }
}