@file:Suppress("PackageName")

package `2023`

import Project
import java.lang.Integer.max
import java.lang.Integer.min

private infix fun Int.toward(to: Int): IntProgression {
    val step = if (this > to) -1 else 1
    return IntProgression.fromClosedRange(this, to, step)
}

class DayFourteen(file: String) : Project() {
/*
    498,4 -> 498,6 -> 496,6
    503,4 -> 502,4 -> 502,9 -> 494,9
*/

    var stuff = HashMap<Int, HashMap<Int, Thing>>()
    val lineCoords = mapFileLines(file) { it.split(" -> ").map { s -> Point(s.split(",")) } }
    var maxY = Int.MIN_VALUE

    enum class Thing {
        ROCK,
        SAND
    }

    class Point(val x: Int, val y: Int) {
        constructor(s: List<String>) : this(s[0].toInt(), s[1].toInt())
    }

    init {
        getStuff()
    }

    private fun getStuff() {
        val ys = HashSet<Int>()
        lineCoords.forEach { line ->
            var curr: Point? = null
            for (coord in line) {
                if (curr != null) {
                    if (curr.x == coord.x) {
                        val x = curr.x
                        for (y in curr.y.toward(coord.y)) {
                            val yMap = stuff[y] ?: HashMap()
                            yMap[x] = Thing.ROCK
                            stuff[y] = yMap
                            ys.add(y)
                        }
                    } else {
                        val y = curr.y
                        ys.add(y)
                        for (x in curr.x.toward(coord.x)) {
                            val yMap = stuff[y] ?: HashMap()
                            yMap[x] = Thing.ROCK
                            stuff[y] = yMap
                        }
                    }
                }
                curr = coord
            }
        }
        maxY = ys.max()
    }

    fun atRest(point: Point): Boolean {
        val row = stuff[point.y + 1] ?: HashMap()
        return listOfNotNull(row[point.x - 1], row[point.x], row[point.x + 1]).size == 3
    }

    fun below(point: Point): Point {
        return Point(point.x, point.y + 1)
    }

    fun left(point: Point): Point {
        return Point(point.x - 1, point.y + 1)
    }

    fun right(point: Point): Point {
        return Point(point.x + 1, point.y + 1)
    }

    override fun part1(): Any {
        var done = false
        var sandCount = 0

        while(!done) {
            var sand = Point(500, 0)

            while (!atRest(sand) && sand.y <= maxY) {
                val row = stuff[sand.y + 1] ?: HashMap()
                if (row[sand.x] == null) {
                    sand = below(sand)
                } else if(row[sand.x - 1] == null) {
                    sand = left(sand)
                } else if(row[sand.x + 1] == null) {
                    sand = right(sand)
                }
            }
            if (sand.y > maxY) {
                done = true
            } else if (atRest(sand)) {
                val row = stuff[sand.y] ?: HashMap()
                row[sand.x] = Thing.SAND
                stuff[sand.y] = row
                sandCount++
            }
        }

        return sandCount
    }

    override fun part2(): Any {
        stuff = HashMap()
        getStuff()

        var done = false
        var sandCount = 0

        while(!done) {
            var sand = Point(500, 0)

            while (!atRest(sand) && sand.y <= maxY) {
                val row = stuff[sand.y + 1] ?: HashMap()
                if (row[sand.x] == null) {
                    sand = below(sand)
                } else if(row[sand.x - 1] == null) {
                    sand = left(sand)
                } else if(row[sand.x + 1] == null) {
                    sand = right(sand)
                }
            }
            if (sand.y == maxY + 1) {
                val row = stuff[sand.y] ?: HashMap()
                row[sand.x] = Thing.SAND
                stuff[sand.y] = row
                sandCount++
            } else if (atRest(sand)) {
                val row = stuff[sand.y] ?: HashMap()
                row[sand.x] = Thing.SAND
                stuff[sand.y] = row
                sandCount++
            }

            done = (stuff[0] ?: HashMap())[500] != null
        }

        printGrid()
        return sandCount
    }

    fun printGrid() {
        for (y in 0..10) {
            for (x in 487..513) {
                val item = (stuff[y] ?: HashMap())[x]
                print(if(item == null) "." else if(item == Thing.SAND) "o" else "#")
            }
            println()
        }
        println("###########################")
    }
}




