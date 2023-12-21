@file:Suppress("PackageName")

package `2023`

import Project

class DaySixteen(file: String) : Project() {
    val grid = getLines(file).map { it.split("").filter { it.isNotBlank() }.map { Tile(it) }}

    class Tile(val it: String, var visitedDirections: MutableList<Direction> = mutableListOf()) {
        fun nextDirections(direction: Direction): List<Direction> {
            if (direction == Direction.RIGHT) {
                return if (it == "." || it == "-") listOf(Direction.RIGHT) else if (it == "/") listOf(Direction.UP) else if (it=="\\") listOf(Direction.DOWN) else listOf(Direction.UP, Direction.DOWN)
            }
            if (direction == Direction.LEFT) {
                return if (it == "." || it == "-") listOf(Direction.LEFT) else if (it == "/") listOf(Direction.DOWN) else if (it=="\\") listOf(Direction.UP) else listOf(Direction.UP, Direction.DOWN)
            }
            if (direction == Direction.DOWN) {

                return if (it == "." || it == "|") listOf(Direction.DOWN) else if (it == "/") listOf(Direction.LEFT) else if (it == "\\") listOf(Direction.RIGHT) else listOf(Direction.LEFT, Direction.RIGHT)
            }

            return if (it == "." || it == "|") listOf(Direction.UP) else if (it == "/") listOf(Direction.RIGHT) else if (it == "\\") listOf(Direction.LEFT) else listOf(Direction.LEFT, Direction.RIGHT)
        }
    }
    class Beam(val x: Int, val y: Int, val direction: Direction) {
        fun step(grid: List<List<Tile>>): List<Beam> {
            val tile = grid[y][x]
            val nextDirections = tile.nextDirections(direction)

            return nextDirections.flatMap {
                val next = mutableListOf<Beam>()
                if (it == Direction.UP && y - 1 >= 0) {
                    next.add(Beam(x, y - 1, it))
                } else if (it == Direction.DOWN && y + 1 < grid.size) {
                    next.add(Beam(x, y + 1, it))
                } else if (it == Direction.LEFT && x - 1 >= 0) {
                    next.add(Beam(x - 1, y, it))
                } else if (it == Direction.RIGHT && x + 1 < grid[0].size) {
                    next.add(Beam(x + 1, y, it))
                }
                next
            }
        }
    }

    enum class Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    override fun part1(): Any {
        var beams = listOf(Beam(0,0, Direction.RIGHT))

        while (beams.isNotEmpty()) {
            beams = beams.flatMap {
                if (!grid[it.y][it.x].visitedDirections.contains(it.direction)) {
                    grid[it.y][it.x].visitedDirections.add(it.direction)
                    it.step(grid)
                } else {
                    listOf()
                }
            }
        }

        return grid.sumOf { it.filter { it.visitedDirections.isNotEmpty() }.size }
    }

    private fun printGrid(beams: List<Beam>) {
        for (y in grid.indices) {
            for (x in grid[0].indices) {
                print(if (beams.any { it.x == x && it.y == y }) "*" else if (grid[y][x].visitedDirections.isNotEmpty()) "#" else ".")
            }
            println()
        }
    }

    override fun part2(): Any {
        return -1
    }
}