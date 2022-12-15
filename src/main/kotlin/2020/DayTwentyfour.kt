@file:Suppress("PackageName")

package `2020`

import Project

class DayTwentyfour(file: String): Project() {
    private val lines = getLines(file)
    private var grid = HashMap<String, Tile>()

    override fun part1(): Any {
        val ns = listOf("n","s")
        val ew = listOf("e","w")

        var skipNext = false
        lines.forEach {
            var tile = Tile(0.0,0.0)

            for (i in it.indices) {
                if (skipNext) {
                    skipNext = false
                    continue
                }
                var dir:String = it[i].toString()
                if (dir in ns && i+1 < it.length && it[i+1].toString() in ew) {
                    skipNext = true
                    dir = it[i].toString() + it[i+1].toString()
                }

                tile = when (dir) {
                    "w" -> Tile(tile.x - 2, tile.y)
                    "e" -> Tile(tile.x + 2, tile.y)
                    "ne" -> Tile(tile.x + 1, tile.y + 1)
                    "nw" -> Tile(tile.x - 1, tile.y + 1)
                    "se" -> Tile(tile.x + 1, tile.y - 1)
                    "sw" -> Tile(tile.x - 1, tile.y - 1)
                    else -> throw Exception("Broken")
                }
            }

            val key = getKey(tile)
            grid.computeIfAbsent(key) { tile }
            grid[key]?.flip()
        }
        return grid.values.filter { it.color == "b" }.size
    }

    private fun getKey(tile: Tile): String {
        return tile.x.toString() + "|" + tile.y.toString()
    }

    override fun part2(): Any {
        for (i in 0 until 100) {
            addNeighbours(grid)
            grid = flip(grid)
        }
        return grid.values.filter { it.color == "b" }.size
    }

    private fun addNeighbours(grid: java.util.HashMap<String, Tile>) {
        val neighbours = HashSet<Tile>()
        grid.values.forEach {
            neighbours.addAll(getNeighbours(it).filter { n -> !grid.containsKey(getKey(n))})
        }

        neighbours.forEach { tile ->
            grid.computeIfAbsent(getKey(tile)) { tile }
        }
    }

    private fun flip(grid: HashMap<String, Tile>): HashMap<String, Tile> {
        val copy = clone(grid)

        copy.values.forEach {
            val c = countNeighbours(it)

            if (it.color == "b") {
                if (c == 0 || c > 2) {
                    it.flip()
                }
            } else {
                if (c == 2) {
                    it.flip()
                }
            }
        }

        return copy
    }

    private fun countNeighbours(tile: Tile): Int {
        return getNeighbours(tile).filter { it.color == "b" }.size
    }

    private fun getNeighbours(tile: Tile): List<Tile> {
        val neighbourKeys = listOf(
            getKey(Tile(tile.x - 2, tile.y)),
            getKey(Tile(tile.x + 2, tile.y)),
            getKey(Tile(tile.x + 1, tile.y + 1)),
            getKey(Tile(tile.x - 1, tile.y + 1)),
            getKey(Tile(tile.x + 1, tile.y - 1)),
            getKey(Tile(tile.x - 1, tile.y - 1))
        )

        return neighbourKeys.map { grid[it] ?: Tile(it.split("|")[0].toDouble(), it.split("|")[1].toDouble()) }
    }

    private fun clone(grid: java.util.HashMap<String, Tile>): HashMap<String, Tile> {
        val newGrid = HashMap<String, Tile>()

        grid.entries.forEach {
            newGrid[it.key] = Tile(it.value.x, it.value.y, it.value.color, it.value.flipCount)
        }

        return newGrid
    }
}

class Tile(val x: Double, val y: Double, var color: String = "w", var flipCount: Int = 0) {
    fun flip() {
        flipCount++
        color = if (color == "w") "b" else "w"
    }

    override fun toString(): String {
        return "x: $x, y:$y, color:$color, flipCount:$flipCount"
    }
}
