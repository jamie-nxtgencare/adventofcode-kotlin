@file:Suppress("PackageName")

package `2023`

import Project
import java.lang.Integer.parseInt
import kotlin.math.abs

class DayEighteen(file: String) : Project() {
    private val cubes = mapFileLines(file) { Cube(it.split(",")) }

    class Cube(val x: Int, val y: Int, val z: Int) {
        constructor(coords: List<String>) : this(parseInt(coords[0]), parseInt(coords[1]), parseInt(coords[2]))

        fun isAdjacentTo(cube: Cube): Boolean {
            val coordDiffs = listOf(abs(x - cube.x), abs(y - cube.y), abs(z - cube.z))

            val sharedCoords = coordDiffs.count { it == 0 }
            val adjacentCoords = coordDiffs.count { it == 1 }

            return sharedCoords == 2 && adjacentCoords == 1
        }

        fun isOutside(minX: Int, maxX: Int, minY: Int, maxY: Int, minZ: Int, maxZ: Int): Boolean {
            return countSidesOutside(minX, maxX, minY, maxY, minZ, maxZ) > 0
        }

        fun countSidesOutside(minX: Int, maxX: Int, minY: Int, maxY: Int, minZ: Int, maxZ: Int): Int {
            return oneIfEqual(x, minX) + oneIfEqual(x, maxX) + oneIfEqual(y, minY) + oneIfEqual(y, maxY) + oneIfEqual(z, minZ) + oneIfEqual(z, maxZ)
        }

        private fun oneIfEqual(a: Int, b: Int): Int {
            return if (a == b) 1 else 0
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Cube) return false

            if (x != other.x) return false
            if (y != other.y) return false
            if (z != other.z) return false

            return true
        }

        override fun hashCode(): Int {
            var result = x
            result = 31 * result + y
            result = 31 * result + z
            return result
        }

        override fun toString(): String {
            return "Cube(x=$x, y=$y, z=$z)"
        }
    }

    override fun part1(): Any {
        var sides = cubes.size * 6

        for (cube in cubes) {
            for (cube2 in cubes) {
                if (cube != cube2 && cube.isAdjacentTo(cube2)) {
                    sides--
                }
            }
        }

        return sides
    }

    override fun part2(): Any {
        val minX = cubes.minOf { it.x }
        val minY = cubes.minOf { it.y }
        val minZ = cubes.minOf { it.z }

        val maxX = cubes.maxOf { it.x }
        val maxY = cubes.maxOf { it.y }
        val maxZ = cubes.maxOf { it.z }

        val emptyCubes = ArrayList<Cube>()
        val outside = ArrayList<Cube>()

        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    val maybeAirCube = Cube(x, y, z)
                    if (!cubes.contains(maybeAirCube)) {
                        emptyCubes.add(maybeAirCube)
                    }
                }
            }
        }

        outside.addAll(emptyCubes.filter { it.isOutside(minX, maxX, minY, maxY, minZ, maxZ) })

        var outsideSize = outside.size
        var newOutsideSize = -1

        while (outsideSize != newOutsideSize) {
            outsideSize = newOutsideSize
            outside.addAll(emptyCubes.filter { !outside.contains(it) && outside.any { o -> it.isAdjacentTo(o) } })
            newOutsideSize = outside.size

            emptyCubes.removeAll(outside.toSet())
        }

        val outsideTouchingCubes = outside.map { o -> Pair(o, cubes.filter { o.isAdjacentTo(it) }) }.filter { it.second.isNotEmpty() }
        val cubeSidesOutside = cubes.sumOf { it.countSidesOutside(minX, maxX, minY, maxY, minZ, maxZ) }

        return outsideTouchingCubes.sumOf { it.second.size } + cubeSidesOutside
    }

}