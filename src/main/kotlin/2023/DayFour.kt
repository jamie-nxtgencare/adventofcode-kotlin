@file:Suppress("PackageName")

package `2023`

import Project
import java.lang.Integer.parseInt

class DayFour(file: String) : Project() {
    private val cards = mapFileLines(file) { Card(it) }

    class Card(s: String) {
        private val split = s.split(": ")
        private val numbersS = split[1].split(" | ")
        private val winners = numbersS[0].split(" ").filter { it.isNotBlank() }.map { parseInt(it.trim()) }.toSet()
        private val numbers = numbersS[1].split(" ").filter { it.isNotBlank() }.map { parseInt(it.trim()) }
        val matches = numbers.filter { winners.contains(it) }.size
        val score = if (matches == 0) 0.0 else Math.pow(2.0, matches.toDouble() - 1)
    }

    override fun part1(): Any {
        return cards.sumOf { it.score }
    }

    override fun part2(): Any {
        return -1.0
    }

}