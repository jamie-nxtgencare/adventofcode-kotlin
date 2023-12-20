@file:Suppress("PackageName")

package `2023`

import Project
import java.lang.Integer.parseInt

class DayTwelve(file: String) : Project() {
    val records = getLines(file).map { it.split(" ") }

    inline fun <T, R,> Iterable<T>.filterThenMap(predicate: (T) -> Boolean, transform: (T) -> R): List<R> {
        return filterThenMap(ArrayList(), predicate, transform)
    }
    inline fun <T, R, C: MutableCollection<in R>>
            Iterable<T>.filterThenMap(collection: C, predicate: (T) -> Boolean,
                                      transform: (T) -> R): C {
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

            val options = mutableListOf(".$springList") // start with a dot so we can tell the code we're a new group
            var i = 0
            for (group in sieve) {
                val matches = mutableListOf<String>()
                val possibleOptions = options.filter { it.count { it == '?' || it == '#' } >= sieve.size - i  }

                for (option in possibleOptions) {
                    var o = option

                    // We need to start with a . to ensure we're starting a new block to match this group
                    if (o.startsWith("?")) {
                        o = o.replaceFirst("?", ".")
                    }

                    if (!o.startsWith(".")) {
                        continue
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

                    matches.addAll(nextOptions.filterThenMap({ it.length >= group.length && it.substring(group.indices).replace("?", "#").startsWith(group) }, { it.replaceRange(group.indices, "") }))
                }
                options.clear()
                options.addAll(matches)
                i++
            }

            val size = options.filter { !it.contains("#") }.size
            println(size)
            size
        }
    }

    override fun part1(): Any {
        return doIt()
    }

    override fun part2(): Any {
        return doIt(5)
    }
}