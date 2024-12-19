import java.io.File
import java.io.FileNotFoundException
import java.time.Duration
import java.time.LocalDateTime
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking

suspend fun checkCancellation() {
    if (!currentCoroutineContext().isActive) {
        throw InterruptedException("Operation cancelled")
    }
}

abstract class Project {
    var sample = false
    private var testMode = false

    companion object {
        var debug = false
    }

    constructor() {
        testMode = false
    }

    constructor(file: String, isTest: Boolean) {
        if (debug) println("Creating Project with file: $file, isTest: $isTest")
        testMode = isTest
        if (debug) println("testMode is now: $testMode")
    }

    // Make part1 and part2 suspendable for cancellation
    abstract suspend fun part1(): Any
    abstract suspend fun part2(): Any

    // Non-suspending versions for Main.kt
    fun part1Blocking(): Any = runBlocking { part1() }
    fun part2Blocking(): Any = runBlocking { part2() }

    fun run(part1Time: LocalDateTime) = runBlocking {
        println(part1())
        println("Part One: %.${2}fms".format(Duration.between(part1Time, LocalDateTime.now()).toNanos()/1_000_000.0))

        val part2Time = LocalDateTime.now()
        println(part2())
        println("Part Two: %.${2}fms".format(Duration.between(part2Time, LocalDateTime.now()).toNanos()/1_000_000.0))
    }

    fun getLines(file: String) : List<String> {
        val path = if (testMode) {
            "src/test/resources/$file"
        } else {
            val packagePath = if (file.contains("/")) {
                file
            } else {
                "${javaClass.packageName}/$file"
            }
            "src/test/resources/$packagePath"
        }

        if (debug) {
            println("getLines called with: $file")
            println("testMode: $testMode")
            println("Trying to load file: $path")
        }
        
        val fileObj = File(path)
        if (!fileObj.exists()) {
            if (debug) println("File not found: $path")
            throw FileNotFoundException("File not found: $path")
        }
        return fileObj.readLines()
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
