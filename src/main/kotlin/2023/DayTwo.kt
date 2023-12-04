@file:Suppress("PackageName")

package `2023`

import Project
import java.lang.Integer.parseInt

class DayTwo(file: String) : Project() {
    val games = mapFileLines(file) { Game(it) }

    override fun part1(): Any {
       return games.filter{ !it.isImpossible() }.sumOf { it.gameId }
    }

    override fun part2(): Any {
        return games.filter{ !it.isImpossible() }.sumOf { it.gameId }
    }

}

class Game(line: String) {
    var gameId: Int;
    var samples: List<Sample>

    init {
        val split = line.split(": ")
        gameId = parseInt(split[0].split(" ")[1])
        samples = split[1].split(";").map { Sample(it.trim()) }
    }

    fun isImpossible(): Boolean {
        return samples.any {
            it.cubes.any {
                var out = it.count > 12
                if (it.color == "blue") {
                    out = it.count > 14
                }
                if (it.color == "green") {
                    out = it.count > 13
                }
                out
            }
        }
    }
}

class Sample(s: String) {
    var cubes: List<CountAndColor>

    init {
        cubes = s.split(",").map { CountAndColor(it.trim().split(" ")) }
    }

}

class CountAndColor(s: List<String>) {
    val count = parseInt(s[0])
    val color = s[1]
}