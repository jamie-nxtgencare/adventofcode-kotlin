@file:Suppress("PackageName")
package `2021`

import Project

class DayFourteen(file: String) : Project() {
    private val lines = getLines(file)
    val template = lines.first()
    var pairs = HashMap<String, String>()

    init {
        for (i in 2 until lines.size) {
            val pair = lines[i].split(" -> ")
            pairs[pair.first()] = pair.last()
        }
    }

    override fun part1(): Any {
        return doItNow(10)
    }

    override fun part2(): Any {
        return doItNow(40)
    }

    private fun doItNow(times: Int): Long {
        var counts = HashMap<String, Long>()
        for (i in 0 until template.length - 1) {
            val sub = template.substring(i, i+2)
            incCount(counts, sub, 1)
        }

        for (i in 0 until times) {
            counts = runLoop(pairs, counts)
        }

        val letterCounts = HashMap<String, Long>()

        counts.forEach {
            val f = it.key[0].toString()
            val l = it.key[1].toString()
            incCount(letterCounts, f, it.value)
            incCount(letterCounts, l, it.value)
        }

        val sorted = letterCounts.entries.sortedBy { it.value }
        var fudge = 0

        if (template.startsWith(sorted.first().key)) {
            fudge -= 1
        }
        if (template.endsWith(sorted.first().key)) {
            fudge -= 1
        }
        if (template.startsWith(sorted.last().key)) {
            fudge += 1
        }
        if (template.endsWith(sorted.last().key)) {
            fudge += 1
        }

        return sorted.last().value/2 - sorted.first().value/2 + fudge
    }

    companion object {
        fun runLoop(pairs: HashMap<String, String>, counts: HashMap<String, Long>): HashMap<String, Long> {
            val entries = ArrayList(counts.entries)
            val newMap = HashMap<String, Long>()
            entries.forEach { e ->
                val n = pairs[e.key]
                incCount(newMap, e.key[0].toString() + n, e.value)
                incCount(newMap, n + e.key[1].toString(), e.value)
            }
            return newMap
        }

        fun incCount(counts: java.util.HashMap<String, Long>, sub: String, v: Long) {
            val count = counts.computeIfAbsent(sub) {0}
            counts[sub] = count + v
        }
    }
}