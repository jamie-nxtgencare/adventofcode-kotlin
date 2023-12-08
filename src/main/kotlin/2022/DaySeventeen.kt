@file:Suppress("PackageName")

package `2022`

import Project
import java.lang.Integer.min
import java.lang.Long.max

private val shapeIntersections = HashMap<Pair<ArrayList<String>, ArrayList<String>>, Boolean>()

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

    class Point(val x: Long, val y: Long)

    class Rock(val shape: List<String>) {
        private val offsetShapes = HashMap<Pair<Long, Long>, ArrayList<String>>()

        fun getOffsetShape(xOffset: Long, yOffset: Long): ArrayList<String> {
            val key = Pair(xOffset, yOffset)
            if (offsetShapes.containsKey(key)) {
                return offsetShapes[key]!!
            }

            var shape = ArrayList(shape)
            for (x in 0..xOffset) {
                shape = ArrayList(shape.map { ".$it" })
            }

            for (y in 0..yOffset) {
                shape.add(0, "")
            }

            offsetShapes[key] = shape

            return shape
        }

        val height = shape.size
        val width = shape.maxOf { it.lastIndexOf("#") + 1 }
    }

    class RockPosition(val point: Point, val rock: Rock) {
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

            val baseX = min(point.x.toInt(), other.point.x.toInt())
            val baseY = max(point.y, other.point.y)

            val shape = getOffsetShape(baseX.toLong(), baseY)
            val otherShape = other.getOffsetShape(baseX.toLong(), baseY)

            return shapesIntersect(shape, otherShape)
        }

        private fun shapesIntersect(shape: java.util.ArrayList<String>, otherShape: java.util.ArrayList<String>): Boolean {
            val key = Pair(shape, otherShape)

            if (shapeIntersections.containsKey(key)) {
                return shapeIntersections[key]!!
            }

            for (y in 0 until min(shape.size, otherShape.size)) {
                val row = shape[y]
                val otherRow = otherShape[y]

                for (x in 0 until min(row.length, otherRow.length)) {
                    if (row[x] == '#' && otherRow[x] == '#') {
                        shapeIntersections[key] = true
                        return true
                    }
                }
            }

            shapeIntersections[key] = false
            return false
        }

        private fun getOffsetShape(baseX: Long, baseY: Long): ArrayList<String> {
            val xOffset = point.x - baseX
            val yOffset = baseY - point.y

            return rock.getOffsetShape(xOffset, yOffset)
        }

        override fun toString(): String {
            return point.toString()
        }
    }

    private fun getHeight(rocks: Long): Long {
        var jetIndex = 0
        var rockCount = 0L
        var rockIndex = 0
        var towerHeight = 0L
        val rockReachableFrom = HashMap<Long, ArrayList<RockPosition>>()
        val lcd = this.rocks.size * jets.length
        var oldTowerHeight = 0L
        println("===Start===")

        val diffs: ArrayList<Long> = ArrayList()
        var maxCycle = 0L
        var previousMaxCycle = 0L
        var maxCycleRepeatCount = 0

        while(rockCount < rocks) {
            rockIndex %= this.rocks.size
            val rock = this.rocks[rockIndex]
            rockIndex++

            val startingPoint = Point(2, rock.height - 1 + towerHeight + 3)
            var rockPosition = RockPosition(startingPoint, rock)
            var falling = true
            var freeFalling = 0

            while(falling) {
                val jet = jets[jetIndex]
                jetIndex = (jetIndex + 1) % jets.length

                val reachableRocks = getReachableRocks(rockPosition, rockReachableFrom)

                var newX = if (jet == '<') rockPosition.left - 1 else rockPosition.left + 1
                var testPosition = RockPosition(Point(newX, rockPosition.top), rock)
                newX = getXPositionAfterJet(newX, testPosition, rockPosition, freeFalling, reachableRocks)

                rockPosition = RockPosition(Point(newX, rockPosition.top), rock)

                val newY = rockPosition.top - 1
                testPosition = RockPosition(Point(rockPosition.left, newY), rock)

                if (freeFalling++ < 3) {
                    rockPosition = testPosition
                } else {
                    if (rockPosition.bottom == 0L || reachableRocks.any { testPosition.intersects(it) }) {
                        falling = false
                    } else {
                        rockPosition = testPosition
                    }

                    if (!falling) {
                        towerHeight = max(towerHeight, rockPosition.top + 1)

                        for (i in rockPosition.top + 4 downTo rockPosition.top - 4) {
                            val collection: ArrayList<RockPosition> = rockReachableFrom[i] ?: ArrayList()
                            collection.add(rockPosition)
                            rockReachableFrom[i] = collection
                        }

                        rockReachableFrom.filterKeys { it < rockPosition.top - 100 }.forEach { rockReachableFrom.remove(it.key) }
                    }
                }
            }

            rockCount++

            if (rockCount % lcd == 0L) {
                val diff = towerHeight - oldTowerHeight
                oldTowerHeight = towerHeight
                println("$diff")
                diffs.add(diff)

                for (i in 1L..400L) {
                    if (hasCycleFor(diffs, i)) {
                        maxCycle = max(i, maxCycle)
                    }
                }
                if (maxCycle != 0L && previousMaxCycle == maxCycle) {
                    maxCycleRepeatCount++
                } else {
                    maxCycleRepeatCount = 0
                }

                if (maxCycleRepeatCount == 3) {
                    println("Definitely found a cycle: $previousMaxCycle")

                    val rocksPerCycle = previousMaxCycle * lcd

                    val rocksRemaining = rocks - rockCount
                    println("Rocks Remaining: $rocksRemaining")

                    val cyclesRemaining = rocksRemaining / rocksPerCycle
                    println("Cycles Remaining: $cyclesRemaining")
                    println("Now what?")

                    rockCount += cyclesRemaining * rocksPerCycle

                    val cycleHeight = diffs.takeLast(previousMaxCycle.toInt()).sum()
                    println("Cycle height: $cycleHeight")

                    val skipHeight = cycleHeight * cyclesRemaining

                    println("Skip height: $skipHeight")
                    println("Tower height: $towerHeight")

                    towerHeight += skipHeight
                    println("Tower height after skipping: $towerHeight")

                    val newRockReachableFromPositions = rockReachableFrom.map { e ->
                        Pair(e.key + skipHeight, e.value.map { r -> RockPosition(Point(r.left, r.top + skipHeight), r.rock) })
                    }

                    rockReachableFrom.clear()
                    newRockReachableFromPositions.forEach { rockReachableFrom[it.first] = ArrayList(it.second) }
                }

                previousMaxCycle = maxCycle
                maxCycle = 0
            }

            if (rockCount % 1_000_000_000L == 0L) {
                println("${rockCount.toDouble() / rocks * 100.0}%")
            }
        }

        return towerHeight
    }

    private fun hasCycleFor(diffs: java.util.ArrayList<Long>, i: Long): Boolean {
        val last = diffs.takeLast(i.toInt())
        val nextLast = diffs.takeLast(i.toInt() * 2)

        if (nextLast.size != i.toInt() * 2) {
            return false
        }

        val first = nextLast.take(i.toInt())
        return first == last && first.size == i.toInt() && last.size == i.toInt()
    }

    private fun getReachableRocks(rockPosition: RockPosition, rockReachableFrom: HashMap<Long, ArrayList<RockPosition>>): ArrayList<RockPosition> {
        return rockReachableFrom[rockPosition.top] ?: ArrayList()
    }

    private fun getXPositionAfterJet(
        newX: Long, testPosition: RockPosition, rockPosition: RockPosition, freeFalling: Int, reachableRocks: ArrayList<RockPosition>
    ): Long {
        if (newX < 0 || testPosition.right == 7L) {
            return rockPosition.left
        } else if (freeFalling > 2 && reachableRocks.any { testPosition.intersects(it) }) {
            return rockPosition.left
        }
        return newX
    }

    override fun part1(): Any {
        return getHeight(2022)
    }

    override fun part2(): Any {
        return getHeight(1_000_000_000_000)
        //return -1
    }

}