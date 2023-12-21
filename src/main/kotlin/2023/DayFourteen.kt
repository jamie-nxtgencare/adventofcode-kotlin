@file:Suppress("PackageName")

package `2023`

import Project
import kotlin.math.floor

class DayFourteen(file: String) : Project() {
    private var grid = getLines(file).map { it.split("").filter { it.isNotBlank() }.toMutableList()}.toMutableList()

    override fun part1(): Any {
        tiltNorth(grid)

        var load = 0
        var distance = grid.size
        for (y in grid.indices) {
            for (x in grid[0].indices) {
                if (grid[y][x] == "O") {
                    load += distance
                }
            }
            distance--
        }

        return load
    }

    override fun part2(): Any {
        val gridCodes = mutableListOf<Int>()
        var targetI = -1
        val cycleCount = 1000000000
        var i = 0
        while (i  < cycleCount - 1) {
            println("i: $i, hashcode: ${grid.hashCode()} load: ${getLoad()}")

            if (targetI > 0 && i < targetI) {
                i = targetI
                continue
            }

            grid = doCycle(grid)
            gridCodes.add(grid.hashCode())

            val reversed = gridCodes.reversed()

            if (targetI < 0) {
                for (j in 1..reversed.size / 2) {
                    val range = 0..j
                    val lastN = reversed.subList(range.first, range.last)
                    val prevN = reversed.subList(range.last, range.last + range.count() - 1)

                    if (lastN == prevN) {
                        println("Loop detected")
                        val remainingCycles = cycleCount - i
                        val remainingLoops = floor(remainingCycles.toDouble() / j).toInt()
                        targetI = i + (remainingLoops * j)
                        gridCodes.clear()
                        break
                    }
                }
            }
            i++
        }

        return getLoad()
    }

    private fun getLoad(): Int {
        var load = 0
        var distance = grid.size
        for (y in grid.indices) {
            for (x in grid[0].indices) {
                if (grid[y][x] == "O") {
                    load += distance
                }
            }
            distance--
        }
        return load
    }

    private fun doCycle(grid: MutableList<MutableList<String>>): MutableList<MutableList<String>> {
        val oldGrid = mutableListOf<MutableList<String>>()
        grid.forEach {
            val row = mutableListOf<String>()
            row.addAll(it)
            oldGrid.add(row)
        }

        tiltNorth(grid)
        tiltWest(grid)
        tiltSouth(grid)
        tiltEast(grid)

        val newGrid = mutableListOf<MutableList<String>>()
        grid.forEach {
            val row = mutableListOf<String>()
            row.addAll(it)
            newGrid.add(row)
        }

        return newGrid
    }

    private fun tiltNorth(grid: MutableList<MutableList<String>>) {
        for (y in 1 until grid.size) {
            for (x in grid[0].indices) {
                if (grid[y][x] == "O") {
                    var newY = y

                    while(newY - 1 >= 0 && grid[newY - 1][x] == ".") {
                        newY--
                    }

                    if (newY >= 0 && newY != y) {
                        grid[y][x] = "."
                        grid[newY][x] = "O"
                    }
                }
            }
        }
    }

    private fun tiltSouth(grid: MutableList<MutableList<String>>) {
        for (y in grid.size - 1 downTo 0) {
            for (x in grid[0].indices) {
                if (grid[y][x] == "O") {
                    var newY = y

                    while(newY + 1 < grid.size && grid[newY + 1][x] == ".") {
                        newY++
                    }

                    if (newY < grid.size && newY != y) {
                        grid[y][x] = "."
                        grid[newY][x] = "O"
                    }
                }
            }
        }
    }

    private fun tiltWest(grid: MutableList<MutableList<String>>) {
        for (x in 1 until grid[0].size) {
            for (y in grid.indices) {
                if (grid[y][x] == "O") {
                    var newX = x

                    while(newX - 1 >= 0 && grid[y][newX - 1] == ".") {
                        newX--
                    }

                    if (newX >= 0 && newX != x) {
                        grid[y][x] = "."
                        grid[y][newX] = "O"
                    }
                }
            }
        }
    }

    private fun tiltEast(grid: MutableList<MutableList<String>>) {
        for (x in grid[0].size - 1 downTo 0) {
            for (y in grid.indices) {
                if (grid[y][x] == "O") {
                    var newX = x

                    while(newX + 1 < grid[0].size && grid[y][newX + 1] == ".") {
                        newX++
                    }

                    if (newX < grid[0].size && newX != x) {
                        grid[y][x] = "."
                        grid[y][newX] = "O"
                    }
                }
            }
        }
    }
}