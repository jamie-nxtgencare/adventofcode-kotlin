@file:Suppress("PackageName")

package `2023`

import Project
import java.lang.Integer.parseInt

class DayTwelve(file: String) : Project() {
    private val records = getLines(file).map { it.split(" ") }

    private inline fun <T, R,> Iterable<T>.filterThenMap(predicate: (T) -> Boolean, transform: (T) -> R): List<R> {
        return filterThenMap(ArrayList(), predicate, transform)
    }
    private inline fun <T, R, C: MutableCollection<in R>> Iterable<T>.filterThenMap(collection: C, predicate: (T) -> Boolean, transform: (T) -> R): C {
        for(element in this) {
            if (predicate(element)) {
                collection.add(transform(element))
            }
        }
        return collection
    }

    private fun doIt(folds: Int = 1): Any {
        return records.sumOf { record ->
            var springList = record[0]
            var damagedStr = record[1]

            if (folds != 1) {
                springList = generateSequence { springList }.take(folds).joinToString("?")
                damagedStr = generateSequence { damagedStr }.take(folds).joinToString(",")
            }

            val damaged = damagedStr.split(",").map { parseInt(it) }
            val sieve = damaged.map { generateSequence { "#" }.take(it).toList().joinToString("") }

            val layerNodes = mutableMapOf(Pair(".$springList", 1L)) // start with a dot so we can tell the code we're a new group

            for ((i, group) in sieve.withIndex()) {
                val possibleOptions = layerNodes.filter { it.key.count { it == '?' || it == '#' } >= sieve.size - i  }
                layerNodes.clear()

                for (option in possibleOptions) {
                    val children = getNextMatches(group, option.key).groupingBy { it }.eachCount()

                    children.forEach {
                        val newValue = option.value * it.value.toLong()
                        val existing = layerNodes[it.key] ?: 0L
                        layerNodes[it.key] = newValue + existing
                    }
                }
            }

            val size = layerNodes.filter { !it.key.contains("#") }.values.sum()
            size
        }
    }

    private fun getNextMatches(group: String, option: String): Collection<String> {
        var o = option

        // We need to start with a . to ensure we're starting a new block to match this group
        if (o.startsWith("?")) {
            o = o.replaceFirst("?", ".")
        }

        if (!o.startsWith(".")) {
            return mutableListOf()
        }

        // trim garbage
        while (o.startsWith(".")) {
            o = o.removeRange(0 until 1)
        }

        val nextOptions = mutableListOf<String>()
        // until we have to make a chunk, fork into a chunk and skipping
        while (o.startsWith("?") || o.startsWith(".")) {
            if (o.startsWith("?")) {
                nextOptions.add(o.replaceFirst("?", "#"))
            }

            o = o.substring(1 until o.length)
        }
        nextOptions.add(o)

        if (o.startsWith(".")) {
            while (o.startsWith(".")) {
                o = o.removeRange(0 until 1)
            }
            nextOptions.add(o)
        }

        return nextOptions.filterThenMap({ it.length >= group.length && it.substring(group.indices).replace("?", "#").startsWith(group) }, { it.replaceRange(group.indices, "") })
    }

    override fun part1(): Any {
        return doIt()
    }

    override fun part2(): Any {
        return doIt(5)
    }
}