@file:Suppress("PackageName")

package `2023`

import Project
import java.lang.Long.parseLong
import java.util.*

class DayFive(file: String) : Project() {
    private val mappers = whitelineSeperatedGrouper(file, { Mapper(it) }, { it })

    class Mapper(lines: List<String>) {
        val seeds = ArrayList<Long>()
        private val sourceRanges = ArrayList<LongRange>()
        private val offsets = ArrayList<Long>()

        init {
            if (lines.size == 1) {
                seeds.addAll(lines[0].split(": ")[1].split(" ").map { parseLong(it.trim()) })
            } else {
                for (line in 1 until lines.size) {
                    val range = lines[line].split(" ")
                    val source = parseLong(range[1])
                    sourceRanges.add(source..source + parseLong(range[2]))
                    offsets.add(parseLong(range[0]) - source)
                }
            }
        }

        fun doMap(needle: Long): Long {
            var out = -1L
            for (i in sourceRanges.indices) {
                if (sourceRanges[i].contains(needle)) {
                    out = needle + offsets[i]
                    break
                }
            }

            return if (out == -1L) needle else out
        }
    }

    override fun part1(): Any {
        val seeds = mappers[0].seeds
        val rest = mappers.subList(1, mappers.size)

        val locations = seeds.map {
            var location = it
            for (mapper in rest) {
                location = mapper.doMap(location)
            }
            location
        }

        return locations.min()
    }

    override fun part2(): Any {
        return -1L
    }

}