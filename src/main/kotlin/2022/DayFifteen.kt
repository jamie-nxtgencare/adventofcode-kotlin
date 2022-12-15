@file:Suppress("PackageName")

package `2022`

import Project
import java.time.Duration
import java.time.Instant
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class DayFifteen(file: String) : Project() {
    private val sensors = mapFileLines(file) { Sensor(it) }

    // Sensor at x=2, y=18: closest beacon is at x=-2, y=15
    class Sensor(s: String) {
        private val sides = s.split(": closest beacon is at ")
        private val coordsS = sides[0].split(" ")
        private val beaconS = sides[1].split(" " )
        private val beaconX = beaconS[0].split("=")[1].replace(",", "").toLong()
        private val beaconY = beaconS[1].split("=")[1].toLong()

        val x = coordsS[2].split("=")[1].replace(",", "").toLong()
        val y = coordsS[3].split("=")[1].toLong()
        private val coveredDistance = abs(beaconY - y) + abs(beaconX - x)

        fun getManhattanLineSegment(y: Long): LineSegment? {
            val yDistance = abs(this.y - y)
            if (yDistance > coveredDistance) {
                return null
            }
            val xDistance = abs(coveredDistance - yDistance)
            return LineSegment(x - xDistance, x + xDistance)
        }

        override fun toString(): String {
            return "Sensor(x=$x, y=$y, coveredDistance=$coveredDistance)"
        }

    }

    class LineSegment(val a: Long, val b: Long) {
        fun overlaps(other: LineSegment): Boolean {
            return a <= other.b && b >= other.a
        }

        fun merge(other: LineSegment): LineSegment {
            return LineSegment(min(a, other.a), max(b, other.b))
        }

        fun size(): Long {
            return b - a
        }

        override fun toString(): String {
            return "LineSegment(a=$a, b=$b)"
        }

        fun contains(x: Long): Boolean {
            return x in a..b
        }

    }

    private fun covered(segmentY: Long): ArrayList<LineSegment> {
        val uncheckedSegments = sensors.map { it.getManhattanLineSegment(segmentY) }.filterNotNull().toMutableList()
        val checked = ArrayList<LineSegment>()

        while (uncheckedSegments.isNotEmpty()) {
            val first = uncheckedSegments.removeFirst()
            var merged = false
            for (segment in uncheckedSegments) {
                if (first.overlaps(segment)) {
                    val new = first.merge(segment)
                    uncheckedSegments.remove(segment)
                    uncheckedSegments.add(0, new)
                    merged = true
                    break
                }
            }
            if (!merged) {
                checked.add(first)
            }
        }
        return checked
    }

    override fun part1(): Any {
        val segmentY = if (sample) 10L else 2000000L
        return covered(segmentY).sumOf { it.size() }
    }

    override fun part2(): Any {
        val segmentY = if (sample) 20L else 4_000_000L

        for (y in 0..segmentY) {
            val covered = covered(y)

            if (covered.size == 1 && covered.first().a < 0 && covered.first().b > segmentY) {
                continue
            }

            for (x in 0..segmentY) {
                if (!covered.any { it.contains(x) }) {
                    return x * 4000000 + y
                }

            }
        }
        return -1
    }

}