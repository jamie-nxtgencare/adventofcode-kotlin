@file:Suppress("PackageName")
package `2021`
import Project
import java.lang.Long.max

class DaySeventeen(file: String, isTest: Boolean = false) : Project(file, isTest) {
    private val ranges = if (file != "empty") parseLine(getLines(file)[0]) else Pair<ClosedRange<Long>, ClosedRange<Long>>(0L..0L,0L..0L)
    private var maxY = Long.MIN_VALUE
    private var countHits = 0

    init {
        for (x in -200L..200L) {
            for (y in -200L..200L) {
                val localMaxY = shoot(Pair(x,y), ranges)
                if (localMaxY != Long.MIN_VALUE) {
                    countHits++
                }
                maxY = max(maxY, localMaxY)
            }
        }
    }

    override suspend fun part1(): Any {
        return maxY
    }

    override suspend fun part2(): Any {
        return countHits
    }

    companion object {
        fun parseLine(line: String): Pair<ClosedRange<Long>, ClosedRange<Long>> {
            val split = line.split(": ")[1].split(", ")
            val xStr = split[0].split("=")[1].split("..")
            val yStr = split[1].split("=")[1].split("..")
            val xRange: ClosedRange<Long> = xStr[0].toLong()..xStr[1].toLong()
            val yRange: ClosedRange<Long> = yStr[0].toLong()..yStr[1].toLong()
            return Pair(xRange, yRange)
        }

        fun shoot(shot: Pair<Long, Long>, ranges: Pair<ClosedRange<Long>, ClosedRange<Long>>): Long {
            var pos = Pair(0L, 0L)
            var vel = Pair(shot.first, shot.second)
            var maxY = Long.MIN_VALUE
            /*
                The probe's x position increases by its x velocity.
                The probe's y position increases by its y velocity.
                Due to drag, the probe's x velocity changes by 1 toward the value 0;
                    that is, it decreases by 1 if it is greater than 0, increases by 1 if it is less than 0, or does not change if it is already 0.
                Due to gravity, the probe's y velocity decreases by 1.
             */

            var hit = false
            while (vel.first != 0L || pos.second > ranges.second.endInclusive) {
                pos = Pair(pos.first + vel.first, pos.second + vel.second)
                vel = Pair(
                    if (vel.first > 0) vel.first - 1 else if (vel.first < 0) vel.first + 1 else 0,
                    vel.second - 1
                )
                if (maxY < pos.second) {
                    maxY = pos.second
                }
                if (isHit(pos, ranges)) {
                    hit = true
                }
                //println(pos)
            }

            return if (hit) maxY else Long.MIN_VALUE
        }

        fun isHit(coord: Pair<Long, Long>, ranges: Pair<ClosedRange<Long>, ClosedRange<Long>>) = ranges.first.contains(coord.first) && ranges.second.contains(coord.second)
    }

}