@file:Suppress("PackageName")
package `2023`
import Day
import Project
import getDayStr
import org.junit.Assert.assertEquals
import org.junit.Test

class Testing {
    @Test
    fun day1Sample() {
        testSample(1, 281, 281)
    }

    @Test
    fun day1() {
        test(1, 54770, 54770)
    }

    @Test
    fun day2Sample() {
        testSample(2, 8, 2286)
    }

    @Test
    fun day2() {
        test(2, 2727, 56580)
    }

    @Test
    fun day3Sample() {
        testSample(3, 4361, 467835)
    }

    @Test
    fun day3() {
        test(3, 536576, 75741499)
    }

    @Test
    fun day4Sample() {
        testSample(4, 13.0, 30)
    }

    @Test
    fun day4() {
        test(4, 25651.0, 19499881)
    }

    @Test
    fun day5Sample() {
        testSample(5, 35L, 46L)
    }

    @Test
    fun day5() {
        test(5, 282277027L, 11554135L)
    }

    @Test
    fun day6Sample() {
        testSample(6, 288, 71503)
    }

    @Test
    fun day6() {
        test(6, 1108800, 36919753)
    }

    @Test
    fun day7Sample() {
        testSample(7, 6440, 5905)
    }

    @Test
    fun day7() {
        test(7, 246163188, 245794069)
    }

    @Test
    fun day8Sample() {
        testSample(8, 2L, 2L)
        testSample(8, 6L, 6L, "2")
        testSample(8, null, 6L, "3")
    }

    @Test
    fun day8() {
        test(8, 17287L, 18625484023687L)
    }

    @Test
    fun day9Sample() {
        testSample(9, -1, -1)
    }

    @Test
    fun day9() {
        test(9, -1, -1)
    }

    @Test
    fun day10Sample() {
        testSample(10, -1, -1)
    }

    @Test
    fun day10() {
        test(10, -1, -1)
    }

    @Test
    fun day11Sample() {
        testSample(11, -1, -1)
    }

    @Test
    fun day11() {
        test(11, -1, -1)
    }

    @Test
    fun day12Sample() {
        testSample(12, -1, -1)
    }

    @Test
    fun day12() {
        test(12, -1, -1)
    }

    @Test
    fun day13Sample() {
        testSample(13, -1, -1)
    }

    @Test
    fun day13() {
        test(13, -1, -1)
    }

    @Test
    fun day14Sample() {
        testSample(14, -1, -1)
    }

    @Test
    fun day14() {
        test(14, -1, -1)
    }

    @Test
    fun day15Sample() {
        testSample(15, -1, -1)
    }

    @Test
    fun day15() {
        test(15, -1, -1)
    }

    @Test
    fun day16Sample() {
        testSample(16, -1, -1)
    }

    @Test
    fun day16() {
        test(16, -1, -1)
    }

    @Test
    fun day17Sample() {
        testSample(17, -1, -1)
    }

    @Test
    fun day17() {
        test(17, -1, -1)
    }

    @Test
    fun day18Sample() {
        testSample(18, -1, -1)
    }

    @Test
    fun day18() {
        test(18, -1, -1)
    }

    @Test
    fun day19Sample() {
        testSample(19, -1, -1)
    }

    @Test
    fun day19() {
        test(19, -1, -1)
    }

    @Test
    fun day20Sample() {
        testSample(20, -1, -1)
    }

    @Test
    fun day20() {
        test(20, -1, -1)
    }

    @Test
    fun day21Sample() {
        testSample(21, -1, -1)
    }

    @Test
    fun day21() {
        test(21, -1, -1)
    }

    @Test
    fun day22Sample() {
        testSample(22, -1, -1)
    }

    @Test
    fun day22() {
        test(22, -1, -1)
    }

    @Test
    fun day23Sample() {
        testSample(23, -1, -1)
    }

    @Test
    fun day23() {
        test(23, -1, -1)
    }

    @Test
    fun day24Sample() {
        testSample(24, -1, -1)
    }

    @Test
    fun day24() {
        test(24, -1, -1)
    }

    @Test
    fun day25Sample() {
        testSample(25, -1, -1)
    }

    @Test
    fun day25() {
        test(25, -1, -1)
    }

    private fun testSample(number: Int, part1: Any?, part2: Any, customSampleFile: String = "") {
        baseTest("2023/day%d.sample-input".format(number), number, part1, part2, true, customSampleFile)
    }

    private fun test(number: Int, part1: Any, part2: Any, customSampleFile: String = "") {
        baseTest("2023/day%d.input".format(number), number, part1, part2, false, customSampleFile)
    }

    private fun baseTest(file: String, number: Int, part1: Any?, part2: Any, sample: Boolean, customSampleFile: String) {
        val day = Day.byNumber(number)
        val dayStr = getDayStr(day)
        @Suppress("UNCHECKED_CAST") val projectClass: Class<out Project> = Class.forName("2023.Day$dayStr") as Class<Project>
        val constructor = projectClass.getDeclaredConstructor(String::class.java)
        val input = file.format(number) + customSampleFile
        var project = constructor.newInstance(input)
        project.sample = sample

        if (part1 != null) {
            assertEquals(part1, project?.part1())
        }

        assertEquals(part2, project?.part2())
    }
}