@file:Suppress("PackageName")
package `2023`
import Day
import Project
import getDayStr
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigInteger

class Testing {
    @Test
    fun day1Sample() {
        testSample(1, 281, 281)
    }

    @Test
    fun day1() {
        test(1, 54770, 199628)
    }

    @Test
    fun day2Sample() {
        testSample(2, 8, 8)
    }

    @Test
    fun day2() {
        test(2, 2727, 12989)
    }

    @Test
    fun day3Sample() {
        testSample(3, 157, 70)
    }

    @Test
    fun day3() {
        test(3, 7889, 2825)
    }

    @Test
    fun day4Sample() {
        testSample(4, 2, 4)
    }

    @Test
    fun day4() {
        test(4, 538, 792)
    }

    @Test
    fun day5Sample() {
        testSample(5, "CMZ", "MCD")
    }

    @Test
    fun day5() {
        test(5, "RLFNRTNFB", "MHQTLJRLB")
    }

    @Test
    fun day6Sample() {
        testSample(6, 11, 26)
    }

    @Test
    fun day6() {
        test(6, 1235, 3051)
    }

    @Test
    fun day7Sample() {
        testSample(7, 95437, 24933642)
    }

    @Test
    fun day7() {
        test(7, 1611443, 2086088)
    }

    @Test
    fun day8Sample() {
        testSample(8, 21, 8)
    }

    @Test
    fun day8() {
        test(8, 1672, 327180)
    }

    @Test
    fun day9Sample() {
        testSample(9, 88, 36)
    }

    @Test
    fun day9() {
        test(9, 6018, 2619)
    }

    @Test
    fun day10Sample() {
        testSample(10, 13140, -1)
    }

    @Test
    fun day10() {
        test(10, 17180, -1)
    }

    @Test
    fun day11Sample() {
        testSample(11, BigInteger.valueOf(10605), BigInteger.valueOf(2713310158))
    }

    @Test
    fun day11() {
        test(11, BigInteger.valueOf(78960), BigInteger.valueOf(14561971968))
    }

    @Test
    fun day12Sample() {
        testSample(12, 31, 29)
    }

    @Test
    fun day12() {
        test(12, 394, 388)
    }

    @Test
    fun day13Sample() {
        testSample(13, 13, 140)
    }

    @Test
    fun day13() {
        test(13, 6076, 24805)
    }

    @Test
    fun day14Sample() {
        testSample(14, 24, 93)
    }

    @Test
    fun day14() {
        test(14, 655, 26484)
    }

    @Test
    fun day15Sample() {
        testSample(15, 26L, 56000011L)
    }

    @Test
    fun day15() {
        test(15, 4811413L, 13171855019123L)
    }

    @Test
    fun day16Sample() {
        testSample(16, 1651, 1707)
    }

    @Test
    fun day16() {
        test(16, 1986, 2464)
    }

    @Test
    fun day17Sample() {
        testSample(17, 3068L, 1514285714288L)
    }

    @Test
    fun day17() {
        test(17, 3232L, 1585632183915L)
    }

    @Test
    fun day18Sample() {
        testSample(18, 64, 58)
    }

    @Test
    fun day18() {
        test(18, 4308, 2540)
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

    private fun testSample(number: Int, part1: Any, part2: Any) {
        baseTest("2023/day%d.sample-input".format(number), number, part1, part2, true)
    }

    private fun test(number: Int, part1: Any, part2: Any) {
        baseTest("2023/day%d.input".format(number), number, part1, part2, false)
    }

    private fun baseTest(file: String, number: Int, part1: Any, part2: Any, sample: Boolean) {
        val day = Day.byNumber(number)
        val dayStr = getDayStr(day)
        @Suppress("UNCHECKED_CAST") val projectClass: Class<out Project> = Class.forName("2023.Day$dayStr") as Class<Project>
        val constructor = projectClass.getDeclaredConstructor(String::class.java)

        val project = constructor.newInstance(file.format(number))
        project.sample = sample

        assertEquals(part1, project?.part1())
        assertEquals(part2, project?.part2())
    }
}