@file:Suppress("PackageName")

package `2020`

import Project

class DayOne(file: String, isTest: Boolean = false) : Project(file, isTest) {
    private val map = getIntLinesToExistsBoolean(file)

    override suspend fun part1(): Any {
        for (entry in map) {
            val a = entry.key
            val b = 2020 - a
            if (map.contains(b)) {
                return a * b
            }
        }

        return -1
    }

    override suspend fun part2(): Any {
        for (e1 in map) {
            for (e2 in map) {
                val a = e1.key
                val b = e2.key
                val c = 2020 - (a + b)
                if (map.contains(c)) {
                    return a * b * c
                }
            }
        }

        return -1
    }
}