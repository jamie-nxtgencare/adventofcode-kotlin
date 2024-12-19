import org.junit.jupiter.api.Assertions.assertEquals as assertEqualsJUnit
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

abstract class TestBase(private val year: String) {
    private val timeoutMs = 5_000L // 5 seconds

    companion object {
        var debug = Project.debug
    }

    protected fun testSample(number: Int, part1: Any?, part2: Any?, customSampleFile: String = "") {
        val time = measureTimeMillis {
            baseTest("day%d.sample-input".format(number), number, part1, part2, true, customSampleFile)
        }
        if (time > 1000) {  // Log tests taking more than 1 second
            if (debug) println("WARNING: Test for day$number sample took ${time}ms")
        }
    }

    protected fun test(number: Int, part1: Any, part2: Any, customSampleFile: String = "") {
        val time = measureTimeMillis {
            baseTest("day%d.input".format(number), number, part1, part2, false, customSampleFile)
        }
        if (time > 1000) {
            if (debug) println("WARNING: Test for day$number took ${time}ms")
        }
    }

    private fun baseTest(file: String, number: Int, part1: Any?, part2: Any?, sample: Boolean, customSampleFile: String) = runBlocking {
        if (debug) {
            println("Initial file path: $file")
            println("Starting test with ${timeoutMs}ms timeout")
        }
        
        withContext(Dispatchers.Default) {
            withTimeout(timeoutMs) {
                val day = Day.byNumber(number)
                if (debug) {
                    println("Day: $day")
                    println("DayStr: ${getDayStr(day)}")
                    println("Year: $year")
                    println("Looking for class: $year.Day${getDayStr(day)}")
                }
                
                @Suppress("UNCHECKED_CAST") 
                val projectClass: Class<out Project> = Class.forName("$year.Day${getDayStr(day)}") as Class<Project>
                
                val input = "$year/$file$customSampleFile"
                if (debug) println("Constructed input path: $input")
                
                val project = try {
                    val constructor = projectClass.getDeclaredConstructor(String::class.java, Boolean::class.java)
                    constructor.newInstance(input, true)
                } catch (e: NoSuchMethodException) {
                    val constructor = projectClass.getDeclaredConstructor(String::class.java)
                    constructor.newInstance(input)
                }
                
                project.sample = sample

                if (debug) println("Running part1")
                if (part1 != null) {
                    yield()
                    assertEqualsJUnit(part1, project.part1())
                }

                if (debug) println("Running part2")
                if (part2 != null) {
                    yield()
                    assertEqualsJUnit(part2, project.part2())
                }
            }
        }
    }

    protected fun assertEquals(expected: Any?, actual: Any?) {
        assertEqualsJUnit(expected, actual)
    }

    protected fun assertPart1(expected: Any, project: Project) {
        if (debug) println("Testing part 1...")
        assertEquals(expected, project.part1Blocking())
        if (debug) println("Part 1 passed!")
    }

    protected fun assertPart2(expected: Any, project: Project) {
        if (debug) println("Testing part 2...")
        assertEquals(expected, project.part2Blocking())
        if (debug) println("Part 2 passed!")
    }

    protected fun assertParts(expected1: Any, expected2: Any, project: Project) {
        if (debug) println("Testing both parts...")
        assertPart1(expected1, project)
        assertPart2(expected2, project)
        if (debug) println("All tests passed!")
    }
} 