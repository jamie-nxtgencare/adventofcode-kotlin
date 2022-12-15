@file:Suppress("PackageName")

package `2018`

import Project
import kotlin.streams.toList

class DayTwo(file: String) : Project() {
    val ids = getLines(file)
    val lines = mapFileLines(file) { it.chars().toList().map { c -> c.toChar().toString() }.groupBy { c -> c }.mapValues { v -> v.value.size } }

    override fun part1(): Any {
       return lines.count { it.any { e -> e.value == 2 } } * lines.count { it.any { e -> e.value == 3} }
    }

    override fun part2(): Any {

        ids.forEach { a ->
            ids.forEach { b ->
                if (a != b) {
                    var count = a.length
                    var diff = ""
                    a.forEachIndexed { i, ai ->
                        if (ai == b[i]) {
                            count--
                            diff += ai
                        }
                    }
                    if (count == 1) {
                        return diff
                    }
                }
            }
        }


        return "-1"
    }

}