@file:Suppress("PackageName")

package `2022`

import Project
import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.collections.ArrayList

class DaySeventeen(file: String) : Project() {
    private val jets = getLines(file)[0]
    private val rocks = listOf(
        Rock(
            listOf(
                "####   "
            )
        ),
        Rock(
            listOf(
                ".#.    ",
                "###    ",
                ".#.    "
            )
        ),
        Rock(
            listOf(
                "..#    ",
                "..#    ",
                "###    "
            )
        ),
        Rock(
            listOf(
                "#      ",
                "#      ",
                "#      ",
                "#      "
            )
        ),
        Rock(
            listOf(
                "##     ",
                "##     "
            )
        )
    )

    class Point(val x: Int, val y: Int) {
        override fun toString(): String {
            return "($x, $y)"
        }
    }

    class Rock(val shape: List<String>) {
        val height = shape.size
        val width = shape.maxOf { it.lastIndexOf("#") + 1 }
    }

    class RockPosition(private val point: Point, private val rock: Rock) {
        val left = point.x
        val right = point.x + rock.width - 1
        val top = point.y
        val bottom = point.y - rock.height + 1

        fun intersects(other: RockPosition): Boolean {
            if (
                other.left > right ||
                other.right < left ||
                other.top < bottom ||
                other.bottom > top
            ) {
                return false
            }

            val baseX = min(point.x, other.point.x)
            val baseY = max(point.y, other.point.y)

            val shape = getOffsetShape(baseX, baseY)
            val otherShape = other.getOffsetShape(baseX, baseY)

            for (y in 0 until min(shape.size, otherShape.size)) {
                val row = shape[y]
                val otherRow = otherShape[y]

                for (x in 0 until min(row.length, otherRow.length)) {
                    if (row[x] == '#' && otherRow[x] == '#') {
                        return true
                    }
                }
            }

            return false
        }

        private fun getOffsetShape(baseX: Int, baseY: Int): ArrayList<String> {
            var shape = ArrayList(rock.shape)
            val xOffset = point.x - baseX
            val yOffset = baseY - point.y

            for (x in 0..xOffset) {
                shape = ArrayList(shape.map { ".$it" })
            }

            for (y in 0..yOffset) {
                shape.add(0, "")
            }

            return shape
        }

        override fun toString(): String {
            return point.toString()
        }
    }

    override fun part1(): Any {
        var jetIndex = 0
        var rockIndex = 0
        var towerHeight = 0
        val fallenRocks = ArrayList<RockPosition>()

        while(rockIndex < 2022) {
            val rock = rocks[rockIndex % rocks.size]

            val startingPoint = Point(2, rock.height - 1 + towerHeight + 3)
            //println("Starting $startingPoint")

            var rockPosition = RockPosition(startingPoint, rock)
            var falling = true

            while(falling) {
                val jet = jets[jetIndex++ % jets.length]
                var newX = if (jet == '<') rockPosition.left - 1 else rockPosition.left + 1
                var testPosition = RockPosition(Point(newX, rockPosition.top), rock)

                if (newX < 0 || testPosition.right == 7) {
                    newX = rockPosition.left
                } else {
                    if (fallenRocks.any { testPosition.intersects(it) }) {
                        newX = rockPosition.left
                    }
                }

                rockPosition = RockPosition(Point(newX, rockPosition.top), rock)

                val newY = rockPosition.top - 1
                testPosition = RockPosition(Point(rockPosition.left, newY), rock)
                if (rockPosition.bottom == 0 || fallenRocks.any { testPosition.intersects(it) }) {
                    falling = false
                } else {
                    rockPosition = testPosition
                }

                /*
                println(jet)
                println(rockPosition)
                */

                if (!falling) {
                    towerHeight = max(towerHeight, rockPosition.top + 1)
                    fallenRocks.add(rockPosition)
                }
            }
            rockIndex++
        }


        return towerHeight
    }

    override fun part2(): Any {
        return -1
    }

}