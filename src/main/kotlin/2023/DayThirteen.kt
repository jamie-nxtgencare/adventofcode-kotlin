@file:Suppress("PackageName")

package `2023`

import Project


class DayThirteen(file: String) : Project() {
    val grids: List<List<String>> = whitelineSeperatedGrouper(file, { it }, { it })

    override fun part1(): Any {
        val out: List<Int> = grids.map {
            var colMatch = getColMatch(it)
            var rowMatch = getColMatch(transpose(it))

            if (colMatch < 0) {
                colMatch = 0
            }

            if (rowMatch < 0) {
                rowMatch = 0
            }

            colMatch + 100 * rowMatch
        }

        return out.sum()
    }

    private fun transpose(it: List<String>): List<String> {
        val newRowStrings = mutableListOf<String>()

        for (i in it[0].indices) {
            var colString = ""
            for (row in it) {
                colString += row[i]
            }
            newRowStrings.add(colString)
        }

        return newRowStrings
    }

    private fun getColMatch(it: List<String>): Int {
        for (i in 0 until it[0].length - 1) {
            var left = 0..i
            var right = i + 1 until it[0].length

            if (left.count() < right.count()) {
                right = i + 1..i + left.count()
            } else {
                left = i - right.count() + 1..i
            }

            val leftStrings = it.map { it.substring(left) }
            val rightStrings = it.map { it.substring(right).reversed() }

            var match = true
            for (j in leftStrings.indices) {
                if (leftStrings[j] != rightStrings[j]) {
                    match = false
                    break
                }
            }

            if (match) {
                val colMatch = i + 1
                return colMatch
            }
        }
        return -1
    }

    override fun part2(): Any {
        return -1
    }
}