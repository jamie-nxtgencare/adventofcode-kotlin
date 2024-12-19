@file:Suppress("PackageName")

package `2023`

import Project
import java.lang.Integer.parseInt
import kotlin.math.min

class DayFour(file: String, isTest: Boolean = false) : Project(file, isTest) {
    private val cards = mapFileLines(file) { Card(it) }

    class Card(s: String) {
        private val split = s.split(": ")
        private val numbersS = split[1].split(" | ")
        private val winners = numbersS[0].split(" ").filter { it.isNotBlank() }.map { parseInt(it.trim()) }.toSet()
        private val numbers = numbersS[1].split(" ").filter { it.isNotBlank() }.map { parseInt(it.trim()) }
        val matches = numbers.filter { winners.contains(it) }.size
        val score = if (matches == 0) 0.0 else Math.pow(2.0, matches.toDouble() - 1)
        var copies = 1
    }

    override suspend fun part1(): Any {
        return cards.sumOf { it.score }
    }

    override suspend fun part2(): Any {
        cards.forEachIndexed { i, card ->
            for (copy in 1 .. card.copies) {
                val winners = cards.subList(min(cards.size, i + 1), min(cards.size, i + 1 + card.matches))
                winners.forEach { it.copies++ }
            }
        }

        return cards.sumOf { it.copies }
    }

}