@file:Suppress("PackageName")

package `2022`

import Project
import java.lang.Integer.parseInt

class DayEighteen(file: String) : Project() {
    val cubes = mapFileLines(file) { Cube(it.split(",")) }

    class Cube(val x: Int, val y: Int, val z: Int) {
        constructor(coords: List<String>) : this(parseInt(coords[0]), parseInt(coords[1]), parseInt(coords[2]))

        fun isAdjacentTo(cube: Cube): Boolean {
            val coordDiffs = listOf(x - cube.x, y - cube.y, z - cube.z)

            val sharedCoords = coordDiffs.count { it == 0 }
            val adjacentCoords = coordDiffs.count { it == 1 }

            return sharedCoords == 2 && adjacentCoords == 1
        }
    }

    override fun part1(): Any {
        var sides = cubes.size * 6

        for (cube in cubes) {
            for (cube2 in cubes) {
                if (cube != cube2 && cube.isAdjacentTo(cube2)) {
                    sides -= 2
                }
            }
        }

        return sides
    }

    override fun part2(): Any {
        return -1
    }

}