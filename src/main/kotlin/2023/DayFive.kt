@file:Suppress("PackageName")

package `2023`

import Project
import java.lang.Long.parseLong

class DayFive(file: String) : Project() {
    private val mappers = whitelineSeperatedGrouper(file, { Mapper(it) }, { it })

    class Mapper(lines: List<String>) {
        val name = lines[0]
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
                    sourceRanges.add(source until source + parseLong(range[2]))
                    offsets.add(parseLong(range[0]) - source)
                }
            }
        }

        fun doMap(seedRanges: List<LongRange>): List<LongRange> {
            val out = ArrayList<LongRange>()

            for (seedRange in seedRanges) {
                //println("\nMapping input range: $seedRange")

                val unmatchedRange = mutableListOf(seedRange)
                for (i in sourceRanges.indices) {
                    if (unmatchedRange.isEmpty()) {
                        break
                    }

                    val checkRanges = ArrayList<LongRange>()
                    checkRanges.addAll(unmatchedRange)
                    unmatchedRange.clear()

                    for (range in checkRanges) {
                        val segments = RangeSegments(range, sourceRanges[i])
                        //println("Trying to map: " + range + " with " + sourceRanges[i] + " offset by: " + offsets[i])

                        if (segments.left != null) {
                            unmatchedRange.add(segments.left!!)
                            //println("Left: " + segments.left!!)
                        }

                        if (segments.overlapping != null) {
                            val mapped = LongRange(segments.overlapping!!.first + offsets[i], segments.overlapping!!.last + offsets[i])
                            out.add(mapped)
                            //println("Mapped: ${segments.overlapping!!} to $mapped")
                        }

                        if (segments.right != null) {
                            unmatchedRange.add(segments.right!!)
                            // println("Right: " + segments.right!!)
                        }
                    }
                }
                out.addAll(unmatchedRange)
            }

            return out
        }
    }

    class RangeSegments(seedRange: LongRange, target: LongRange) {
        var left: LongRange? = null
        var overlapping: LongRange? = null
        var right: LongRange? = null

        init {
            if (seedRange.last < target.first) { // fully left
                left = seedRange
            } else if(seedRange.first > target.last) { // fully right
                right = seedRange
            } else if (target.first <= seedRange.first && target.last >= seedRange.last) { // fully covered
                overlapping = seedRange
            } else if (seedRange.first < target.first && seedRange.last > target.last) { // fully covers
                left = LongRange(seedRange.first, target.first - 1)
                overlapping = target
                right = LongRange(target.last + 1, seedRange.last)
            } else if (seedRange.first < target.first) { // overlaps left
                left = LongRange(seedRange.first, target.first - 1)
                overlapping = LongRange(target.first, seedRange.last)
            } else { // overlaps right
                overlapping = LongRange(seedRange.first, target.last)
                right = LongRange(target.last + 1, seedRange.last)
            }
        }
    }

    override fun part1(): Any {
        println("Part 1 ---------")
        val seeds = mappers[0].seeds
        val rest = mappers.subList(1, mappers.size)

        val seedRanges = ArrayList<List<LongRange>>()

        for (seed in seeds) {
            seedRanges.add(mutableListOf(LongRange(seed, seed)))
        }

        val ranges = seedRanges.flatMap {
            var location = it
            for (mapper in rest) {
                location = mapper.doMap(location)
            }
            location
        }

        return ranges.minOf { it.first }
    }

    override fun part2(): Any {
        //println("Part 2 ---------")
        val seedRangeSetup = mappers[0].seeds
        val seedRanges = ArrayList<List<LongRange>>()

        for (i in 0 until seedRangeSetup.size step 2) {
            seedRanges.add(mutableListOf(LongRange(seedRangeSetup[i], seedRangeSetup[i]+seedRangeSetup[i+1]-1)))
        }

        val rest = mappers.subList(1, mappers.size)

        val ranges = seedRanges.flatMap {
            var location = it
            for (mapper in rest) {
                //println("\nDoing mapper: ${mapper.name}")
                location = mapper.doMap(location)
            }
            location
        }

/*        ranges.sortedBy { it.first }.forEach {
            println(it)
        }*/

        return ranges.minOf { it.first }
    }

}