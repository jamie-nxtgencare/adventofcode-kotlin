import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigInteger

class Testing {
    @Test
    fun day1Sample() {
        testSample(1, 514579, 241861950)
    }

    @Test
    fun day1() {
        test(1, 445536, 138688160)
    }

    @Test
    fun day2Sample() {
        testSample(2, 2, 1)
    }

    @Test
    fun day2() {
        test(2, 460, 251)
    }

    @Test
    fun day3Sample() {
        testSample(3, 7, 336)
    }

    @Test
    fun day3() {
        test(3, 220, 2138320800)
    }

    @Test
    fun day4Sample() {
        testSample(4, 2, 2)
    }

    @Test
    fun day4() {
        test(4, 245, 133)
    }

    @Test
    fun day5Sample() {
        testSample(5, 820, -1)
    }

    @Test
    fun day5() {
        test(5, 878, 504)
    }

    @Test
    fun day6Sample() {
        testSample(6, 11, 6)
    }

    @Test
    fun day6() {
        test(6, 6587, 3235)
    }

    @Test
    fun day7Sample() {
        testSample(7, 4, 32)
    }

    @Test
    fun day7() {
        test(7, 185, 89084)
    }

    @Test
    fun day8Sample() {
        testSample(8, 5, 8)
    }

    @Test
    fun day8() {
        test(8, 1818, 631)
    }

    @Test
    fun day9Sample() {
        testSample(9, BigInteger.valueOf(127), BigInteger.valueOf(62))
    }

    @Test
    fun day9() {
        test(9, BigInteger.valueOf(15353384), BigInteger.valueOf(2466556))
    }

    private fun testSample(number: Int, part1: Any, part2: Any) {
        baseTest("day%d.sample-input".format(number), number, part1, part2)
    }

    private fun test(number: Int, part1: Any, part2: Any) {
        baseTest("day%d.input".format(number), number, part1, part2)
    }

    private fun baseTest(file: String, number: Int, part1: Any, part2: Any) {
        val day = Day.byNumber(number)
        val dayStr = getDayStr(day)
        @Suppress("UNCHECKED_CAST") val projectClass: Class<out Project> = Class.forName("Day$dayStr") as Class<Project>
        val constructor = projectClass.getDeclaredConstructor(String::class.java)

        val project = constructor.newInstance(file.format(number))

        assertEquals(project?.part1(), part1)
        assertEquals(project?.part2(), part2)
    }
}