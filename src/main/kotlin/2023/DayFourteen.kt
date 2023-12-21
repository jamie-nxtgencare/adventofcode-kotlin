@file:Suppress("PackageName")

package `2023`

import Project

class DayFourteen(file: String) : Project() {
    val grid = getLines(file).map { it.split("").filter { it.isNotBlank() }.toMutableList()}.toMutableList()

    override fun part1(): Any {
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
        return -1
    }
}