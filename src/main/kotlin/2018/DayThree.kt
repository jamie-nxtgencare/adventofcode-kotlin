@file:Suppress("PackageName")

package `2018`

import Project

class DayThree(file: String, isTest: Boolean = false) : Project(file, isTest) {
    val lines = getLines(file)
    val covered = HashMap<Int, HashMap<Int, ArrayList<String>>>()

    init {
        lines.forEach {
            //#1 @ 1,3: 4x4
            val sides = it.split(":")
            val labelAndStart = sides[0].split("@ ")
            val label = labelAndStart[0].trim()
            val startxy = labelAndStart[1].split(",")
            val startx = startxy[0].toInt()
            val starty = startxy[1].toInt()
            val size = sides[1].trim().split("x")
            val sizex = size[0].toInt()
            val sizey = size[1].toInt()

            for (y in starty until starty + sizey) {
                val row = covered[y] ?: HashMap()
                for (x in startx until startx + sizex) {
                    val coveredBy = row[x] ?: ArrayList()
                    coveredBy.add(label)
                    row[x] = coveredBy
                }
                covered[y] = row
            }
        }
    }

    override suspend fun part1(): Any {
        return covered.values.map { it.values.count { v -> v.size > 1 } }.sum()
    }

    override suspend fun part2(): Any {
        val labels: Set<String> = covered.values.flatMap { it.values.filter { v -> v.size == 1 }.map { it.first() } }.toSet()

        val positions = covered.values.flatMap { it.values }

        for (label in labels) {
            if (positions.all { !it.contains(label) || it.size == 1 }) {
                return label.replace("#", "").toInt()
            }
        }

        return -1
    }

}