@file:Suppress("PackageName")

package `2022`

import Project

class DayEight(file: String) : Project {
    val grid = mapLettersPerLines(file) { it.map { it2 -> it2.toString().toInt() }.toList() }.toList()

    override fun part1(): Any {
        var visible = 0
        for (y in grid.indices) {
            for (x in grid[y].indices) {
                if (isVisible(x, y)) {
                    visible++
                }
            }
        }

        printGrid(grid)
        return visible
    }

    private fun isVisible(x: Int, y: Int): Boolean {
        if (x == 0 || y == 0 || x == grid[y].size - 1 || y == grid.size - 1) {
            return true
        }

        val leftVisible = checkRow(0, x, x, y)
        val rightVisible = checkRow(x + 1, grid[y].size, x, y)
        val topVisible = checkCol(0, y, x, y)
        val bottomVisible = checkCol(y + 1, grid.size, x, y)

        return leftVisible || rightVisible || topVisible || bottomVisible
    }

    private fun checkRow(start: Int, end: Int, x:Int, y: Int): Boolean {
        var visible = true
        for (i in start until end) {
            if (!visible) {
                break
            }

            visible = grid[y][i] < grid[y][x]
        }

        return visible
    }

    private fun checkCol(start: Int, end: Int, x:Int, y: Int): Boolean {
        var visible = true
        for (i in start until end) {
            if (!visible) {
                break
            }

            visible = grid[i][x] < grid[y][x]
        }

        return visible
    }

    override fun part2(): Any {
        return -1
    }

    fun printGrid(grid: List<List<Int>>) {
        grid.forEachIndexed { y, it ->
            it.forEachIndexed { x, o ->
                print(if (isVisible(x, y)) "#" else ".")
            }
            println()
        }
        println()
    }
}