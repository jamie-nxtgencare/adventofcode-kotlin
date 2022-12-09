@file:Suppress("PackageName")

package `2022`

import Project

class DayNine(file: String) : Project {
    private val motions = mapFileLines(file) { it.split(" ") }.map { Motion(it[0], it[1].toInt()) }
    private var head = Point(0, 0)
    private var tail = Point(0, 0)
    private val tailLocations = HashSet<Point>()

    init {
        reset()
    }

    private fun reset() {
        head = Point(0, 0)
        tail = Point(0, 0)
        tailLocations.clear()
    }

    class Point(val x: Int, val y: Int) {
        fun movePoint(direction: Direction): Point {
            if (direction == Direction.UP) {
                return Point(x, y + 1)
            }
            if (direction == Direction.DOWN) {
                return Point(x, y - 1)
            }
            if (direction == Direction.LEFT) {
                return Point(x - 1, y)
            }
            return Point(x + 1, y)
        }

        fun getTail(tail: Point): Point {
            val dX = x - tail.x
            val dY = y - tail.y

            if (dX < -1) {
                return Point(x + 1, y)
            }
            if (dX > 1) {
                return Point(x - 1, y)
            }
            if (dY < -1) {
                return Point(x, y + 1)
            }
            if (dY > 1) {
                return Point(x, y - 1)
            }
            return tail
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Point) return false

            if (x != other.x) return false
            if (y != other.y) return false

            return true
        }

        override fun hashCode(): Int {
            var result = x
            result = 31 * result + y
            return result
        }

        override fun toString(): String {
            return "($x, $y)"
        }
    }

    class Motion(dirString: String, val steps: Int) {
        val direction = Direction.values().first { it.dirString == dirString }
    }

    enum class Direction(val dirString: String) {
        UP("U"),
        DOWN("D"),
        LEFT("L"),
        RIGHT("R")
    }

    override fun part1(): Any {
        tailLocations.add(tail)

        motions.forEach { motion ->
            for (i in 1..motion.steps) {
                head = head.movePoint(motion.direction)
                tail = head.getTail(tail)
                tailLocations.add(tail)
                println("$head $tail")
            }
        }
        return tailLocations.size
    }

    override fun part2(): Any {
        reset()
        return -1
    }

}