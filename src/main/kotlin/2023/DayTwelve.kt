@file:Suppress("PackageName")

package `2023`

import Project
import java.lang.Integer.parseInt

class DayTwelve(file: String) : Project() {
    val records = getLines(file).map { it.split(" ") }
    override fun part1(): Any {
        return records.map { record ->
            val springList = record[0]
            val damaged = record[1].split(",").map { parseInt(it) }

            val maxQs = springList.count { it == '?' }
            val options = listOf("#", ".")
            val combinations = getCombos(options, listOf(""), maxQs)

            val guesses = combinations.map { combo ->
                var thisCombo = springList
                combo.forEach {
                    thisCombo = thisCombo.replaceFirst('?', it)
                }
                thisCombo
            }

            val guessDamageCounts = guesses.map { it.split(".").filter { it.isNotBlank() }.map{it.length } }
            guessDamageCounts.filter { it == damaged }.size
        }.sum()
    }

    // ...
    // .#.
    // ..#
    // #..
    // ##.
    // #.#
    // ###
    private fun getCombos(options: List<String>, prefixed: List<String>, maxQs: Int): List<String> {
        val out = mutableListOf<String>()

        if (maxQs == 1) {
            return prefixed.flatMap { prefix ->
                options.map {
                    prefix + it
                }
            }
        }

        val next = prefixed.flatMap { prefix ->
            options.flatMap {
                val next = mutableListOf<String>()
                next.add(prefix + it)
                next
            }
        }

        return getCombos(options, next, maxQs -1)
    }

    override fun part2(): Any {
        return -1
    }
}