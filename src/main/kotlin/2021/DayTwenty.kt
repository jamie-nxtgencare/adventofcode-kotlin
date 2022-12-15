@file:Suppress("PackageName")
package `2021`

import Project

class DayTwenty(file: String) : Project() {
    private val lines = getLines(file)
    private val key = lines[0].split("").filter { it.isNotBlank() }.map { if (it == "#") 1 else 0 }
    private val image: List<List<Int>> = lines.subList(2, lines.size).map { it.split("").filter { s -> s.isNotBlank() }.map { pixel -> if (pixel == "#") 1 else 0 } }

    override fun part1(): Any {
        var workingGrid = ArrayList(image.map { ArrayList(it) })
        workingGrid = growGrid(workingGrid)

        for (i in 0..1) {
            workingGrid = enhance(workingGrid)
        }

        workingGrid.map {
            it[0] = 0
            it[it.size-1] = 0
        }

        return workingGrid.sumOf { it.sum() }
    }

    override fun part2(): Any {
        var workingGrid = ArrayList(image.map { ArrayList(it) })
        workingGrid = growGrid(workingGrid)

        for (i in 0..49) {
            workingGrid = enhance(workingGrid)
        }

        workingGrid.map {
            for (i in 0..49) {
                it[i] = 0
                it[it.size - 1 - i] = 0
            }
        }

        return workingGrid.sumOf { it.sum() }
    }

    fun enhance(grid: ArrayList<ArrayList<Int>>): ArrayList<ArrayList<Int>> {
        val newGrid = ArrayList(grid.map { ArrayList(it) })

        for (row in grid.indices) {
            for (column in grid[row].indices) {
                val n = neighbours(grid, Pair(column, row))
                val k = n.joinToString("").toInt(2)
                newGrid[row][column] = key[k]
            }
        }

        return newGrid
    }

    private fun growGrid(grid: java.util.ArrayList<java.util.ArrayList<Int>>): java.util.ArrayList<java.util.ArrayList<Int>> {
        var newGrid = ArrayList(grid.map { ArrayList(it) })

        val addBuffer = 100

        newGrid = ArrayList(newGrid.map { row ->
            val newRow = ArrayList<Int>()
            for (i in 0..addBuffer) {
                newRow.add(0)
            }
            newRow.addAll(row)
            newRow

        })
        for (row in newGrid) {
            for (i in 0..addBuffer) {
                row.add(0)
            }
        }

        val empty = arrayOfNulls<Int>(newGrid[0].size)
        empty.fill(0)
        val emptyList = ArrayList(empty.toList()).map { it!! }

        val bufferedGrid = ArrayList<ArrayList<Int>>()
        for (i in 0..addBuffer) {
            bufferedGrid.add(ArrayList(emptyList as ArrayList<Int>))
        }
        bufferedGrid.addAll(newGrid)
        newGrid = bufferedGrid
        for (i in 0..addBuffer) {
            newGrid.add(ArrayList(emptyList as ArrayList<Int>?))
        }

        return newGrid
    }

    companion object {
        fun neighbours(grid: ArrayList<ArrayList<Int>>, coord: Pair<Int, Int>): List<Int> {
            val x= coord.first
            val y = coord.second

            return listOf(
                safeGet(grid, x - 1, y - 1),
                safeGet(grid, x,     y - 1),
                safeGet(grid, x + 1, y - 1),
                safeGet(grid, x - 1, y),
                safeGet(grid, x,     y),
                safeGet(grid, x + 1, y),
                safeGet(grid, x - 1, y + 1),
                safeGet(grid, x,     y + 1),
                safeGet(grid, x + 1, y + 1),
            )
        }

        private fun safeGet(grid: ArrayList<ArrayList<Int>>, x: Int, y: Int): Int {
            return if (y >= 0 && y < grid.size && x >= 0 && x < grid[y].size) grid[y][x] else 0
        }
    }

}