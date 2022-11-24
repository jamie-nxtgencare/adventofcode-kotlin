@file:Suppress("PackageName")
package `2021`

import Project

class DayTwentyfive(file: String) : Project {
    val grid: ArrayList<ArrayList<String>> = ArrayList(mapLettersPerLines(file) { ArrayList(it.map { c -> c.toString() }) })

    override fun part1(): Any {

        var count = 0
        var moved = true

        while (moved) {
            val movedEast = move(true)
            val movedSouth = move(false)
            moved = movedEast || movedSouth
            count++
        }

        return count
    }

    override fun part2(): Any {
        return -1
    }

    fun move(east: Boolean): Boolean {
        val moveCoords = ArrayList<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
        if (east) {
            grid.forEachIndexed { y, row ->
                row.forEachIndexed { x, col ->
                    if (col == ">" && grid[y]!![(x+1)%grid[y].size] == ".") {
                        moveCoords.add(Pair(Pair(x, y), Pair((x+1)%grid[y].size, y)))
                    }
                }
            }
        } else {
            grid.forEachIndexed { y, row ->
                row.forEachIndexed { x, col ->
                    if (col == "v" && grid[(y+1)%grid.size]!![x] == ".") {
                        moveCoords.add(Pair(Pair(x, y), Pair(x, (y+1)%grid.size)))
                    }
                }
            }
        }

        moveCoords.forEach {
            val oldCoords = it.first
            val newCoords = it.second
            grid[newCoords.second]!![newCoords.first] = grid[oldCoords.second]!![oldCoords.first]
            grid[oldCoords.second]!![oldCoords.first] = "."
        }

        return moveCoords.size > 0
    }

}