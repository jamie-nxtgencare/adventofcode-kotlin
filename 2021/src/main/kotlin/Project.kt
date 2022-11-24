import java.io.File
import java.time.Duration
import java.time.LocalDateTime

interface Project {
    fun part1(): Any
    fun part2(): Any

    fun run(part1: LocalDateTime) {
        println(part1())
        println("Part One: %.${2}fms".format(Duration.between(part1, LocalDateTime.now()).toNanos()/1_000_000.0))

        val part2 = LocalDateTime.now()
        println(part2())
        println("Part Two: %.${2}fms".format(Duration.between(part2, LocalDateTime.now()).toNanos()/1_000_000.0))
    }

    fun getLines(file: String) : List<String> {
        return File({}.javaClass.getResource(file).file).readLines()
    }

    fun <R> mapFileLines(file: String, mapper: (String) -> R) : List<R> {
        return getLines(file).map(mapper)
    }

    /******
     * Example:
     *
     * 1721
     * 979
     * 366
     *
     * becomes: [1721: true, 979: true, 366: true ]
     */
    fun getIntLinesToExistsBoolean(file: String): Map<Int, Boolean> {
        return mapFileLines(file) { Integer.parseInt(it) to true }.toMap()
    }

    fun <R> mapLettersPerLines(file: String, mapper: (List<Char>) -> R) : List<R> {
        return mapFileLines(file) { it.toCharArray().asList() }.map(mapper)
    }

    fun <INNER, GROUP> whitelineSeperatedGrouper(file: String, groupMaker: (List<INNER>) -> GROUP, innerMaker: (String) -> INNER): List<GROUP> {
        val output = ArrayList<GROUP>()
        var group = ArrayList<INNER>()
        getLines(file).forEach { line ->
            if (line.isBlank()) {
                output.add(groupMaker(group))
                group = ArrayList()
            } else {
                group.add(innerMaker(line))
            }
        }
        output.add(groupMaker(group))

        return output
    }

    fun printGrid(grid: ArrayList<ArrayList<Int>>) {
        grid.forEach {
            it.forEach { o ->
                print(if (o == 1) "#" else ".")
            }
            println()
        }
        println()
    }
}
