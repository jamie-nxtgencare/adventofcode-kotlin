@file:Suppress("PackageName")

package `2022`

import Project

class DayNine(file: String, isTest: Boolean = false) : Project(file, isTest) {
    private val motions = mapFileLines(file) { it.split(" ") }.map { Motion(it[0], it[1].toInt()) }

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

            var newX = tail.x
            var newY = tail.y
            var movedX = false
            var movedY = false

            if (dX < -1) {
                newX = x + 1
                movedX = true
            }
            if (dX > 1) {
                newX = x - 1
                movedX = true
            }
            if (dY < -1) {
                newY = y + 1
                movedY = true
            }
            if (dY > 1) {
                newY = y - 1
                movedY = true
            }

            if (movedX && movedY) {
                return Point(newX, newY)
            } else if (movedX) {
                return Point(newX, y)
            } else if (movedY) {
                return Point(x, newY)
            }

            return Point(newX, newY)
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
    }

    class Motion(dirString: String, val steps: Int) {
        val direction = Direction.values().first { it.dirString == dirString }

        override fun toString(): String {
            return "\n\n== Motion(direction=$direction, steps=$steps) ==\n\n"
        }
    }

    enum class Direction(val dirString: String) {
        UP("U"),
        DOWN("D"),
        LEFT("L"),
        RIGHT("R")
    }

    override suspend fun part1(): Any {
        var head = Point(0, 0)
        var tail = Point(0, 0)
        val tailLocations = HashSet<Point>()

        tailLocations.add(tail)

        motions.forEach { motion ->
            for (i in 1..motion.steps) {
                head = head.movePoint(motion.direction)
                tail = head.getTail(tail)
                tailLocations.add(tail)
            }
            //println(motion)
            //printGrid(head, arrayListOf(tail))
        }
        return tailLocations.size
    }

    override suspend fun part2(): Any {
        var head = Point(0, 0)
        val tails = arrayListOf(Point(0,0), Point(0,0), Point(0,0), Point(0,0), Point(0,0), Point(0,0), Point(0,0), Point(0,0), Point(0,0))
        val tailLocations = HashSet<Point>()

        tailLocations.add(tails[8])
        //printGrid(head, tails)

        motions.forEach { motion ->
            for (j in 1..motion.steps) {
                head = head.movePoint(motion.direction)

                var subHead = head
                tails.forEachIndexed { i, tail ->
                    tails[i] = subHead.getTail(tail)
                    subHead = tails[i]
                }

                tailLocations.add(tails[8])
                //printGrid(head, tails)
            }
        }

        return tailLocations.size
    }

/*    private fun printGrid(head: Point, tails: ArrayList<Point>) {
        for (y in 25 downTo -25) {
            for (x in -25..25) {
                val point = Point(x, y)
                if (head == point) {
                    print("H")
                } else if (tails.indexOf(point) >= 0) {
                    print(tails.indexOf(point) + 1)
                } else {
                    print(".")
                }
            }
            println()
        }
    }*/
}