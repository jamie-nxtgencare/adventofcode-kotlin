@file:Suppress("PackageName")

package `2022`

import Project

class DayFour(file: String) : Project {
    private val sections = mapFileLines(file) { Section(it) }
    // 2-4,6-8

    class Section(line: String) {
        private val range1 = getRange(line.split(",")[0])
        private val range2 = getRange(line.split(",")[1])

        private fun getRange(s: String): IntRange {
            return IntRange(s.split("-")[0].toInt(), s.split("-")[1].toInt())
        }

        fun fullyOverlaps(): Boolean {
            return (range1.first >= range2.first && range1.last <= range2.last) || (range2.first >= range1.first && range2.last <= range1.last)
        }
    }

    override fun part1(): Any {
        return sections.filter { it.fullyOverlaps() }.count()
    }

    override fun part2(): Any {
        return -1
    }

}